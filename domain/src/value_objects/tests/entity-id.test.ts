import { EntityId, EntityIdSchema } from '../entity-id';
import { createStringValueObjectCreationTests } from './domain-primitive-test-helper';

createStringValueObjectCreationTests({
  ctor: EntityId,
  schema: EntityIdSchema,
  validInputs: [
    'b03543dc-2037-4dc3-90c8-9bba426d2b0d',
    '743ef038-c311-4e12-9dfb-5c68f90d898a',
    'c9de61ed-6914-498f-a205-9d432211e218',
    '9b821dc9-467d-4fc5-8260-4718e45a3bcc',
    '366bf143-d260-4fa8-b949-d90d80a0d333',
    '8d3b0307-dc8f-463d-8664-0c4567bbf185',
    '964b5cf9-b854-4e25-9f62-bfdfc6d3ab42',
    'a76bd09f-9989-4c18-8695-6818649fb11f',
    '13a2a309-c100-4bde-8819-53035c684cf3',
    '8a70353b-baf3-4bed-be24-06020a62691c',
    '24752949-fcb3-4d9e-8714-3b24514db2aa',
    '348d01f6-6f15-49d7-bb71-6d5a72c97a70',
    '6aae9831-f12b-4d24-8f0d-e4690b7dc69b',
    'f00b3c4c-9c9a-45d2-9383-b6d4673e0ac5',
    '7364d31b-d85f-4a4d-ba7d-6b7cc5704ab4',
    'bc6c8c12-2a46-45db-a179-755abc14516e',
    '95f7a646-c3f3-4a4f-a921-7ffb480ed165',
    'eace5ba1-742a-469a-8a71-9839fd9ddd86',
    '22e6bee6-fe3e-4541-9d3d-faf4923d1d5e',
    'b3bfd953-fc6f-4911-832c-43eb37020552',
  ],
  invalidInputs: [
    '03543dc-2037-4dc3-90c8-9bba426d2b0d',
    '743ef038-311-4e12-9dfb-5c68f90d898a',
    'c9de61ed-6914-98f-a205-9d432211e218',
    '9b821dc9-467d-4fc5-260-4718e45a3bcc',
    '366bf143-d260-4fa8-b949-90d80a0d333',
    '18d3b0307-dc8f-463d-8664-0c4567bbf185',
    '964b5cf9-2b854-4e25-9f62-bfdfc6d3ab42',
    'a76bd09f-9989-34c18-8695-6818649fb11f',
    '13a2a309-c100-4bde-48819-53035c684cf3',
    '8a70353b-baf3-4bed-be24-506020a62691c',
  ]
})

