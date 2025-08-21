
package dev.demo;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;
import software.amazon.awssdk.services.ssm.model.SsmException;

public class App {
  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: java -jar app.jar <SECRET_ID> <PARAM_NAME>");
      System.exit(2);
    }
    final String secretId = args[0];
    final String paramName = args[1];

    final String regionName = System.getenv().getOrDefault("AWS_REGION", "ap-south-1");
    final Region region = Region.of(regionName);

    try (SecretsManagerClient sm = SecretsManagerClient.builder().region(region).build();
         SsmClient ssm = SsmClient.builder().region(region).build()) {

      GetSecretValueResponse s = sm.getSecretValue(
          GetSecretValueRequest.builder().secretId(secretId).build());
      String secret = s.secretString() != null ? s.secretString() : "<binary secret>";

      GetParameterResponse p = ssm.getParameter(
          GetParameterRequest.builder().name(paramName).withDecryption(true).build());
      String param = p.parameter() != null ? p.parameter().value() : null;

      System.out.println("Region : " + regionName);
      System.out.println("Secret : " + secretId + " = " + secret);
      System.out.println("Param  : " + paramName + " = " + param);

    } catch (SecretsManagerException e) {
      System.err.println("Secrets Manager error: " + e.awsErrorDetails().errorMessage());
      System.exit(1);
    } catch (SsmException e) {
      System.err.println("SSM error: " + e.awsErrorDetails().errorMessage());
      System.exit(1);
    }
  }
}
