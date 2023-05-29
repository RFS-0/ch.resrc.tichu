import { z } from 'zod';

type Implements<Model> = {
  [key in keyof Model]-?: // make all keys of Model required
  undefined extends Model[key] // check if undefined allowed
  ? null extends Model[key] // undefined allowed -> check if null allowed
  ? z.ZodNullableType<z.ZodOptionalType<z.ZodType<Model[key]>>> // undefined and null allowed -> make optional and nullable
  : z.ZodOptionalType<z.ZodType<Model[key]>> // undefined allowed -> make optional
  : null extends Model[key] // undefined not allowed -> check if null allowed
  ? z.ZodNullableType<z.ZodType<Model[key]>> // null allowed -> make nullable
  : z.ZodType<Model[key]>; // null not allowed -> make required
};

type EnsureNoUnknownKeys<ValidKeys, KeysToBeChecked> = {
  [unknownKey in Exclude<keyof ValidKeys, keyof KeysToBeChecked>]: never;
}

export function implement<Model = never>() {
  return {
    // TODO: this does not compile correctly becasue of an issue with the keyof operator
    // extend: <SuperModel extends ZodRawShape>(baseSchema: ZodObject<SuperModel>) => {
    //   return {
    //     with: <Schema extends Implements<Omit<Model, keyof SuperModel>> & EnsureNoUnknownKeys<Schema, Model>>(schema: Schema) => {
    //       return baseSchema.extend(schema)
    //     }
    //   };
    // },
    with: <Schema extends Implements<Model> & EnsureNoUnknownKeys<Schema, Model>>(schema: Schema) => z.object(schema),
  };
}

export function extend<Model, BaseSchema extends z.ZodObject<z.ZodRawShape>>(baseSchema: BaseSchema) {
  return {
    with: <Schema extends Implements<Model> & EnsureNoUnknownKeys<Schema, Model>>(
      schema: Schema
    ) => baseSchema.extend(schema),
  };
}
