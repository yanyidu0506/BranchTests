Run command:

git clone https://github.com/yanyidu0506/BranchTests.git 

cd BranchTests

mvn clean package -Dplatform=mac

The test cases can be executed on mac and linux platform. Can be extended to any platform by passing a mvn run parameter. Here I provide 2 platforms to run. 

All test cases are using testng and maven to run. The computer to run the tests require chrom and firefox(better to have latest version) and maven 3.3.9 version or later installed.  testng lib jars are downloaded by maven dependency management.

The test report can be found at target/surefire-reports/index.html. All test report files are under target/surefire-reports/ folder after the execution.

The execution command to run tests:
"mvn clean package -Dplatform=mac"

If you execute on linxu platform, you run: 
"mvn clean package -Dplatform=linux64"

The -Dplatform is the parameter to point out which platform to run the test cases. Currently you can pass mac or linux64. This parameter will be pass to maven maven-surefire-plugin configure part of pom.xml. We have 2 testng.xml files that are testng-mac.xml and testng-linux64.xml and the platform parameter will be used to find out which testng xml to run. 

Each test case run on both chrom and firefox browsers.
Set up 4 test cases for below test cases. 

testNumbersOfEmployees   :  verify if the count of "all" tag page has the same number to total count of other dept tag pages. We found there are 4 more employee in all tag page than other dept tag.

testEachTeamNameInAllPage    :    verify each employee name at each dept tag page has the same value in all tag page.

testNameInAllTeamPageToEachTeamPage    :    verify if employee name in all tag page matched each dept tag.  We found there are 
4 employees in all tag page are not in any dept tag page.

testDeptOfEmployees   :    verify if the dept of each employee at all tag page has the same value in each dept tag page.


