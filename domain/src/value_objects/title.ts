import { z } from 'zod';
import { implement } from '../validation';
import { RawStringValueObject, StringValueObject } from './value-object';

export interface RawTitle extends RawStringValueObject { }

const titleRegex = /^(?!.*[\r\n])[\p{L}\s]{1,255}$/u

export const TitleSchema = implement<RawTitle>().with({
  value: z.string().regex(titleRegex, `Invalid Title`)
});

export class Title extends StringValueObject {
  constructor(input: RawTitle) {
    super(input);
    const result = TitleSchema.safeParse(input)
    if (!result.success) {
      throw new Error(`Preconditions to create ${Title.name} not met`)
    }
    this._value = result.data.value
  }
}
