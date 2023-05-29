export default {
  clearMocks: true,
  collectCoverage: true,
  coverageDirectory: "dist/coverage",
  coveragePathIgnorePatterns: [
    "/node_modules/",
    "/tests/",
  ],
  coverageProvider: "v8",
  preset: "ts-jest",
  testMatch: [
    "**/?(*.)+(spec|test).[tj]s?(x)"
  ]
};
