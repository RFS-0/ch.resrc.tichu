import { Title, TitleSchema } from '../title';
import { createStringValueObjectCreationTests } from './domain-primitive-test-helper';

createStringValueObjectCreationTests({
  ctor: Title,
  schema: TitleSchema,
  validInputs: [
    'This is a valid title',
  ],
  invalidInputs: [
    'Invalid!', // because !
  ]
})

