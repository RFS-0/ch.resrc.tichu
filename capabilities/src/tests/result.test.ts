import { Result } from '../result';

describe(`Testing the 'Result'`, () => {
  describe("when it is a 'Failure'", () => {
    test("that it is a failure", () => {
      const failure = Result.failure(["some error"]);

      expect(failure.isFailure()).toBe(true);
      expect(failure.isSuccess()).toBe(false);
      expect(failure.isEmpty()).toBe(false);
    });

    test("that it throws an error when trying to get the value", () => {
      const failure = Result.failure(["some error"]);

      expect(() => failure.value()).toThrowError();
    });


    test("that it does not throw an error when mapping the value", () => {
      const failure = Result.failure(["some error"]);

      expect(() => failure.map(() => "map should do nothing")).not.toThrowError();
    });

    test("that it does not modify the value when mapping the value", () => {
      const failure = Result.failure(["some error"]);

      const result = failure.map(() => "map should do nothing");

      expect(result.isFailure()).toBe(true);
    });

    test("that it short circuits when flatMapping the value", () => {
      const failure = Result.failure(["some error"]);

      const shouldNotBeCalled = () => { throw new Error("should not be called") };

      expect(() => failure.flatMap(() => shouldNotBeCalled())).not.toThrow()
    });
  });

  describe("when it is an 'EmptyFailure'", () => {
    test("that it is an empty failure", () => {
      const failure = Result.emptyFailure(["some error"]);

      expect(failure.isFailure()).toBe(true);
      expect(failure.isSuccess()).toBe(false);
      expect(failure.isEmpty()).toBe(true);
    });

    test("that it throws an error when trying to get the value", () => {
      const failure = Result.emptyFailure(["some error"]);

      expect(() => failure.value()).toThrowError();
    });


    test("that it throws an error when mapping the value", () => {
      const failure = Result.emptyFailure(["some error"]);

      expect(() => failure.map(() => "map should fail")).toThrowError();
    });

    test("that it throws an error when flat mapping the value", () => {
      const failure = Result.emptyFailure(["some error"]);

      const shouldNotBeCalled = () => { throw new Error("should not be called") };

      expect(() => failure.flatMap(() => shouldNotBeCalled())).toThrow()
    });
  });

  describe("when it is a 'Success'", () => {
    test("that it is a success", () => {
      const success = Result.success("success");

      expect(success.isFailure()).toBe(false);
      expect(success.isSuccess()).toBe(true);
      expect(success.isEmpty()).toBe(false);
    });

    test("that it does not throw an error when trying to get the value", () => {
      const success = Result.success("success");

      expect(() => success.value()).not.toThrow();
    });


    test("that it does not throw an error when mapping the value", () => {
      const success = Result.success("success");

      expect(() => success.map(() => "map should do something")).not.toThrowError();
    });

    test("that it does modify the value when mapping the value", () => {
      const success = Result.success("success");

      const result = success.map(() => "mapped value");

      expect(result.value()).toBe("mapped value");
    });

    test("that it returns the flatMapped value when flatMapping the value", () => {
      const success = Result.success("success");

      const flatMapped = success.flatMap((value) => Result.success(value + " flatMapped"));

      expect(flatMapped.value()).toBe("success flatMapped");
    });
  });
});

// describe(`Testing the 'AsyncResult'`, () => {
//   describe("when it is a 'AsyncFailure'", () => {
//     test("that it is a failure", async () => {
//       const failure = AsyncResult.failure(pendingPromise(["some error"]))
//       expect(failure.isFailure()).toBe(true);
//       expect(failure.isSuccess()).toBe(false);
//       expect(failure.isEmpty()).toBe(false);
//     });

//     test("that it throws an error when trying to get the value", () => {
//       const failure = AsyncResult.failure(pendingPromise(["some error"]));

//       expect(() => failure.value()).toThrowError();
//     });


//     test("that it does not throw an error when mapping the value", () => {
//       const failure = AsyncResult.failure(pendingPromise(["some error"]));

//       expect(() => failure.map(() => pendingPromise("should never be called"))).not.toThrowError();
//     });

//     test("that it does not modify the value when mapping the value", () => {
//       const failure = AsyncResult.failure(pendingPromise(["some error"]));

//       const result = failure.map(() => pendingPromise("map should do nothing"));

//       expect(result.isFailure()).toBe(true);
//     });

//     test("that it short circuits when flatMapping the value", () => {
//       const failure = AsyncResult.failure(pendingPromise(["some error"]));

//       const shouldNotBeCalled = () => { throw new Error("should not be called") };

//       expect(() => failure.flatMap(() => shouldNotBeCalled())).not.toThrow()
//     });
//   });

//   describe("when it is an 'EmptyFailure'", () => {
//     test("that it is an empty failure", () => {
//       const failure = AsyncResult.emptyFailure(pendingPromise(["some error"]));

//       expect(failure.isFailure()).toBe(true);
//       expect(failure.isSuccess()).toBe(false);
//       expect(failure.isEmpty()).toBe(true);
//     });

//     test("that it throws an error when trying to get the value", () => {
//       const failure = AsyncResult.emptyFailure(pendingPromise(["some error"]));

//       expect(() => failure.value()).toThrowError();
//     });


//     test("that it throws an error when mapping the value", () => {
//       const failure = AsyncResult.emptyFailure(pendingPromise(["some error"]));

//       expect(() => failure.map(() => pendingPromise("map should fail"))).toThrowError();
//     });

//     test("that it throws an error when flat mapping the value", () => {
//       const failure = AsyncResult.emptyFailure(pendingPromise(["some error"]));

//       const shouldNotBeCalled = () => { throw new Error("should not be called") };

//       expect(() => failure.flatMap(() => shouldNotBeCalled())).toThrow()
//     });
//   });

//   describe("when it is a 'Success'", () => {
//     test("that it is a success", () => {
//       const success = AsyncResult.success(pendingPromise("success"));

//       expect(success.isFailure()).toBe(false);
//       expect(success.isSuccess()).toBe(true);
//       expect(success.isEmpty()).toBe(false);
//     });

//     test("that it does not throw an error when trying to get the value", () => {
//       const success = AsyncResult.success(pendingPromise("success"));

//       expect(() => success.value()).not.toThrow();
//     });

//     test("that it does resolve the expected value when trying to get the value", async () => {
//       const success = AsyncResult.success(resolvedPromise("success"));

//       expect(await success.value()).toBe("success");
//     });

//     test("that it does not throw an error when mapping the value", () => {
//       const success = AsyncResult.success(resolvedPromise("success"));

//       expect(() => success.map(() => resolvedPromise("map should do something"))).not.toThrowError();
//     });

//     test("that it does modify the value when mapping the value", () => {
//       const success = AsyncResult.success(resolvedPromise("success"));

//       const result = success.map(() => resolvedPromise("mapped value"));

//       expect(result.value()).resolves.toBe("mapped value");
//     });

//     test("that it returns the flatMapped value when flatMapping the value", () => {
//       const success = AsyncResult.success(resolvedPromise("success"));

//       const flatMapped = success
//         .flatMap((value) => AsyncResult.success(value.then(resolved => resolvedPromise(resolved + " flatMapped"))));

//       expect(flatMapped.value()).resolves.toBe("success flatMapped");
//     });
//   });

//   describe("when the Promise is rejected, the AsyncResult should be a AsyncFailure", () => {

//     // test("that it throws an error when trying to get the value", () => {
//     //   const failure = AsyncResult.success(rejectedPromise("some error"));

//     //   expect(() => failure.value()).toThrowError();
//     // });
//   });
// });
