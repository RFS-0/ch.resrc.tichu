import { AsyncResult } from '../async-result';
import { Problem } from '../problem';
import { Result } from '../result';

describe(`Testing the 'AsyncResult'`, () => {

  describe("when the initial operation will succeed", () => {

    test("that the value is correct", () => {
      const result = AsyncResult.fromPromise(() => Promise.resolve(1));
      expect(result.get()).resolves.toEqual(Result.success(1));
    });
  });

  describe("when multiple operations are chained and all operations will succeed", () => {

    test("that the value can be mapped", () => {
      const result = AsyncResult.fromPromise(() => Promise.resolve(1))
        .map(value => value + 1)

      expect(result.get()).resolves.toEqual(Result.success(2));
    });

    test("that the value can be flat mapped", () => {
      const result = AsyncResult.fromPromise(() => Promise.resolve(1))
        .flatMap(() => AsyncResult.fromPromise(() => Promise.resolve("flatMapped")))

      expect(result.get()).resolves.toEqual(Result.success("flatMapped"));
    });

    test("that the value can be flat mapped using the previous value", () => {
      const result = AsyncResult.fromPromise(() => Promise.resolve(1))
        .flatMap((value) => AsyncResult.fromPromise(() => Promise.resolve(value + "flatMapped")))

      expect(result.get()).resolves.toEqual(Result.success("1flatMapped"));
    });

    test("that the value can be mapped multiple times", () => {
      const result = AsyncResult.fromPromise(() => Promise.resolve(1))
        .map(value => value + 1)
        .map(value => value + 1)

      expect(result.get()).resolves.toEqual(Result.success(3));
    });

    test("that the value can be flat mapped multiple times", () => {
      const result = AsyncResult.fromPromise(() => Promise.resolve(1))
        .flatMap(() => AsyncResult.fromPromise(() => Promise.resolve("flatMapped")))
        .flatMap(() => AsyncResult.fromPromise(() => Promise.resolve("flatMappedAgain")))

      expect(result.get()).resolves.toEqual(Result.success("flatMappedAgain"));
    });

    test("that the value can be consumed multiple times", async () => {
      const result = await AsyncResult.fromPromise(() => Promise.resolve(1))
        .doEffect(value => expect(value).toBe(1))
        .map(value => value + 1)
        .doEffect(value => expect(value).toBe(2))
        .map(value => value + 1)
        .doEffect(value => expect(value).toBe(3))
        .get();
      expect(result).toEqual(Result.success(3));
    });
  });

  describe("when the initial operation will fail", () => {

    test("that the failure is catched when the promise fails with an unknown error", () => {
      const result = AsyncResult.fromPromise(() => Promise.reject(undefined));
      expect(result.get()).resolves.toEqual(Result.failure([Problem.withMessage("Unknown error")]));
    });

    test("that the failure is catched when the promise fails with an error", () => {
      const result = AsyncResult.fromPromise(() => Promise.reject(new Error("error")));
      expect(result.get()).resolves.toEqual(Result.failure([Problem.withMessage("error")]));
    });

    test("that the failure is catched when the promise fails with a problem", () => {
      const result = AsyncResult.fromPromise(() => Promise.reject(Problem.withMessage("error")));
      expect(result.get()).resolves.toEqual(Result.failure([Problem.withMessage("error")]));
    });

    test("that the failure is catched when the promise fails with multiple problems", () => {
      const result = AsyncResult.fromPromise(() => Promise.reject([Problem.withMessage("error1"), Problem.withMessage("error2")]));
      expect(result.get()).resolves.toEqual(Result.failure([Problem.withMessage("error1"), Problem.withMessage("error2")]));
    });

    test("that the errors can be mapped", () => {
      const result = AsyncResult.fromPromise(() => Promise.reject(Problem.withMessage("initial error")))
        .map(() => Promise.resolve(1))
        .mapErrors((errors) => errors.map(error => Problem.withMessage(error.message + " mapped error")));

      expect(result.get()).resolves.toEqual(Result.failure([Problem.withMessage("initial error mapped error")]));
    });

    test("that the errors can be mapped multiple times", () => {
      const result = AsyncResult.fromPromise(() => Promise.reject(Problem.withMessage("initial error")))
        .map(() => Promise.resolve(1))
        .mapErrors((errors) => errors.map(error => Problem.withMessage(error.message + " map")))
        .mapErrors((errors) => errors.map(error => Problem.withMessage(error.message + " another map")));

      expect(result.get()).resolves.toEqual(Result.failure([Problem.withMessage("initial error map another map")]));
    });

    test("that the errors can be consumed multiple times", async () => {
      const errors = await AsyncResult.fromPromise(() => Promise.reject(Problem.withMessage("initial error")))
        .doFailureEffect(errors => expect(errors).toEqual([Problem.withMessage("initial error")]))
        .mapErrors((errors) => errors.map(error => Problem.withMessage(error.message + " map")))
        .doFailureEffect(errors => expect(errors).toEqual([Problem.withMessage("initial error map")]))
        .mapErrors((errors) => errors.map(error => Problem.withMessage(error.message + " another map")))
        .doFailureEffect(errors => expect(errors).toEqual([Problem.withMessage("initial error map another map")]))
        .get();

      expect(errors).toEqual(Result.failure([Problem.withMessage("initial error map another map")]));
    });

    test("that mapping a failure short circuits", async () => {
      const result = await AsyncResult.fromPromise(() => Promise.reject(Problem.withMessage("initial error")))
        .map(() => Promise.resolve(1))
        .get();

      expect(result).toEqual(Result.failure([Problem.withMessage("initial error")]));
    });

    test("that flat mapping a failure short circuits", async () => {
      const result = await AsyncResult.fromPromise(() => Promise.reject(Problem.withMessage("initial error")))
        .flatMap(() => AsyncResult.fromPromise(() => Promise.resolve(1)))
        .get();

      expect(result).toEqual(Result.failure([Problem.withMessage("initial error")]));
    });

    test("that mapping a failure short circuits at first failure", async () => {
      const result = await AsyncResult.fromPromise(() => Promise.resolve("success"))
        .map(() => Promise.reject(Problem.withMessage("first error")))
        .get();

      expect(result).toEqual(Result.failure([Problem.withMessage("first error")]));
    });

    test("that flat mapping a failure short circuits at first failure", async () => {
      const result = await AsyncResult.fromPromise(() => Promise.resolve("success"))
        .flatMap(() => AsyncResult.fromPromise(() => Promise.reject(Problem.withMessage("first error"))))
        .get();

      expect(result).toEqual(Result.failure([Problem.withMessage("first error")]));
    });

    test("that consuming a failure short circuits", async () => {
      const result = await AsyncResult.fromPromise(() => Promise.reject(Problem.withMessage("initial error")))
        .doEffect(() => { throw new Error("should not be called") })
        .get();

      expect(result).toEqual(Result.failure([Problem.withMessage("initial error")]));
    });

    test("that the failure can be recovered from with a promise", async () => {
      const result = await AsyncResult.fromPromise<string>(() => Promise.reject("initial error"))
        .tryRecoverWithPromise(() => Promise.resolve("recovered"))
        .get();

      expect(result).toEqual(Result.success("recovered"));
    });

    test("that the failure can be recovered from with a another result", async () => {
      const result = await AsyncResult.fromPromise<string>(() => Promise.reject("initial error"))
        .tryRecoverWithResult(() => AsyncResult.fromValue("recovered"))
        .get();

      expect(result).toEqual(Result.success("recovered"));
    });
  });
});
