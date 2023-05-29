export function getKeyByValue<
  TEnumKey extends string,
  TEnumVal extends string | number
>(
  myEnum: { [key in TEnumKey]: TEnumVal },
  enumValue: TEnumVal
): TEnumKey | undefined {
  return (Object.keys(myEnum) as TEnumKey[]).find(
      (x) => myEnum[x] === enumValue
  )
}
