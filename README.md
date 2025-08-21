
# aws-sm-ssm-demo (Gradle, Java 17)

A tiny Java (AWS SDK v2) console app that fetches one value from **AWS Secrets Manager** and one from **SSM Parameter Store**—mirroring the Node.js sample.

## Prereqs
- Java 17+ (or change toolchain to 11 in `build.gradle.kts`)
- Gradle installed locally **or** generate a wrapper with: `gradle wrapper --gradle-version 8.8`

## Configure AWS credentials/region
The SDK uses the default provider chain. Easiest on Lightsail/EC2 is env vars:
```bash
export AWS_REGION=ap-south-1
export AWS_ACCESS_KEY_ID=AKIA...
export AWS_SECRET_ACCESS_KEY=...
```
(If the instance has an attached IAM role, you usually don't need keys.)

## Build & run
```bash
# Run without packaging
./gradlew run --args='"my/app/secret" "/my/app/db_password"'

# Or build a fat JAR
./gradlew shadowJar
java -jar build/libs/aws-sm-ssm-demo-1.0.0-all.jar "my/app/secret" "/my/app/db_password"
```

## IAM policy
Attach to the user/role used by the app:
```json
{
  "Version": "2012-10-17",
  "Statement": [
    { "Effect": "Allow", "Action": ["secretsmanager:GetSecretValue"], "Resource": "*" },
    { "Effect": "Allow", "Action": ["ssm:GetParameter"], "Resource": "*" }
  ]
}
```
If your SecureString/Secret uses a CMK, also allow `kms:Decrypt` on that key.
