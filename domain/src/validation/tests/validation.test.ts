import {implement} from '../validation';
import {z} from 'zod';
import {RawPlayer} from '../../entities';

describe(`Testing the schema validation`, () => {
    describe("when implementing a model with a schema", () => {
        test("the schema must specify all properties of the model", () => {
            interface Model {
                prop1: string
            }

            const schema = implement<Model>().with({
                prop1: z.string()
            });

            const result = schema.safeParse({
                prop1: 'value'
            })

            expect(result.success).toBe(true);
        })

        test("the schema must not specify additional properties", () => {
            interface Model {
                prop1: string
            }

            const schema = implement<Model>().with({
                prop1: z.string(),
                // uncommenting the following line should cause a compilation error
                // prop2: z.string()
            });

            const result = schema.safeParse({
                prop1: 'value'
            })

            expect(result.success).toBe(true);
        })

        test("the schema makes all properties of the model required by default", () => {
            interface Model {
                prop1?: string
            }

            const schema = implement<Model>().with({
                prop1: z.string().optional()
                // removing .optional() should cause a compilation error
                // prop1: z.string()
            });

            const result = schema.safeParse({
                prop1: 'value'
            })

            expect(result.success).toBe(true);
        })

        test("the schema works with enums", () => {
            enum MyEnum {
                Value1 = 'value1',
            }

            interface Model {
                prop1: MyEnum
            }

            const schema = implement<Model>().with({
                prop1: z.nativeEnum(MyEnum),
            });

            const result = schema.safeParse({
                prop1: MyEnum.Value1
            })

            expect(result.success).toBe(true);
        })

        test("the schema works with nested schemas", () => {
            interface NestedModel {
                prop2: number
            }

            const nestedSchema = implement<NestedModel>().with({
                prop2: z.number()
            });

            interface Model {
                prop1: string
                nested: NestedModel
            }

            const schema = implement<Model>().with({
                prop1: z.string(),
                nested: nestedSchema
            });

            const result = schema.safeParse({
                prop1: 'value',
                nested: {
                    prop2: 1
                }
            })

            expect(result.success).toBe(true);
        })

        test("the schema works with arrays of nested schemas", () => {
            interface NestedModel {
                prop2: number
            }

            const nestedSchema = implement<NestedModel>().with({
                prop2: z.number()
            });

            interface Model {
                prop1: string
                nested: NestedModel[]
            }

            const schema = implement<Model>().with({
                prop1: z.string(),
                nested: z.array(nestedSchema)
            });

            const result = schema.safeParse({
                prop1: 'value',
                nested: [
                    {
                        prop2: 1
                    }
                ]
            })

            expect(result.success).toBe(true);
        })

        test("the schema works with extended schemas", () => {
            interface BaseModel {
                baseProp: number
            }

            const baseSchema = implement<BaseModel>().with({
                baseProp: z.number()
            });

            interface Model extends BaseModel {
                prop1: string
            }

            // this does not work because baseProp is missing
            // const schema = implement<Model>().with({
            //     prop1: z.string(),
            // });

            // this works because baseProp is present - but changes in baseSchema are not reflected in schema
            // const schema = implement<Model>().with({
            //     baseProp: z.number(),
            //     prop1: z.string(),
            // });

            // intended way of extending schemas
            const schema = implement<Model>()
                .extend(baseSchema)
                .with({
                    prop1: z.string(),
                });

            const result = schema.safeParse({
                baseProp: 1,
                prop1: 'value'
            })

            expect(result.success).toBe(true);
        })
    })
})
