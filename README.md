# health-check-tool

- Checks the health of REST APIs using SpringBoot framework
- REST APIs to be monitored are added to YAML file
- Loops thru the YAML file and invokes each service one at a time
- Time taken for service call, JSON output and health indicator are returned in response
- After setup, navigate to http://localhost:8080/health/check/ on browser
