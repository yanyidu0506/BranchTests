package com.yanyi.company.branch;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestCases {
    private static final String WEBDRIVER_CHROME_DRIVER = "webdriver.chrome.driver";
    private static final String WEBDRIVER_GECKO_DRIVER = "webdriver.gecko.driver";
    private WebDriver driver;
    private String googleUrl;
    private String branchUrl;
	private JavascriptExecutor js;
	private WebDriverWait wait;
	private String teamUrl;	
	private String allUrl;
	private String dataUrl;
	private String engineeringUrl;
	private String marketingUrl;
	private String operationsUrl;
	private String partnerGrowthUrl;
	private String productUrl;
	private String recruitingUrl;
	private String xpathInlineEmployeesOnPages;
	
	@Parameters({"browserType", "platform"})
	@BeforeClass
	public void setUp(String browserType, String platform) throws InterruptedException {
        if (browserType.equalsIgnoreCase("firefox") && platform.equalsIgnoreCase("mac")) {
            System.setProperty(WEBDRIVER_GECKO_DRIVER, "geckodriverMac");
            driver = new FirefoxDriver();
        } else if (browserType.equalsIgnoreCase("chrome") && platform.equalsIgnoreCase("mac")) {
            System.setProperty(WEBDRIVER_CHROME_DRIVER, "chromedriverMac");
            driver = new ChromeDriver();
        } else if (browserType.equalsIgnoreCase("firefox") && platform.equalsIgnoreCase("linux64")) {
            System.setProperty(WEBDRIVER_GECKO_DRIVER, "geckodriverLinux64");
            driver = new FirefoxDriver();
        } else if (browserType.equalsIgnoreCase("chrome") && platform.equalsIgnoreCase("linux64")) {
            System.setProperty(WEBDRIVER_CHROME_DRIVER, "chromedriverLinux64");
            driver = new ChromeDriver();
        }	    
        xpathInlineEmployeesOnPages = "//div[@style='display: inline-block;']";	    
		wait = new WebDriverWait(driver, 15);
		js = (JavascriptExecutor) driver;
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		
		getAllBranchUrls();
        driver.get(teamUrl);
	}

	@Test
	public void testNumbersOfEmployees() throws InterruptedException {
        //get all team persons
	    List<WebElement> allTeamPersons = getTeamMembers("all");
        //get data team persons
	    List<WebElement> dataTeamPersons = getTeamMembers("data");
        //get all team persons
	    List<WebElement> engiTeamPersons = getTeamMembers("engineering");
        //get all team persons
	    List<WebElement> marketTeamPersons = getTeamMembers("marketing");
        //get all team persons
	    List<WebElement> opTeamPersons = getTeamMembers("operations");
        //get all team persons
	    List<WebElement> partnerTeamPersons = getTeamMembers("partner-growth");
        //get all team persons
	    List<WebElement> productTeamPersons = getTeamMembers("product");
        //get all team persons
	    List<WebElement> recruitTeamPersons = getTeamMembers("recruiting");
        
	    int allTeamCount = allTeamPersons.size();
	    int totalCountOfSubTeams = dataTeamPersons.size() + 
	            engiTeamPersons.size() + 
	            marketTeamPersons.size() + 
	            opTeamPersons.size() + 
	            partnerTeamPersons.size() + 
	            productTeamPersons.size() + 
	            recruitTeamPersons.size();
        assertEquals(totalCountOfSubTeams, allTeamCount, " The employee total number at All page is not equal to sum of all other team pages! " + 
	            "All team page has number : " + allTeamCount + ", the sum of other teams has number : " + totalCountOfSubTeams);
	}
	
	@Test
	public void testEachTeamNameInAllPage() throws InterruptedException {
        //get all team persons
	    List<WebElement> allTeamPersons = getTeamMembers("all");
        Set<String> allNames = findTeamMemberNames(allTeamPersons);        
        
        //get data team persons
        List<WebElement> dataTeamPersons = getTeamMembers("data");
        Set<String> dataNames = findTeamMemberNames(dataTeamPersons);

        //get engineering team persons
        List<WebElement> engiTeamPersons = getTeamMembers("engineering");
        Set<String> engiNames = findTeamMemberNames(engiTeamPersons);

        //get marketing team persons
        List<WebElement> marketTeamPersons = getTeamMembers("marketing");
        Set<String> marketNames = findTeamMemberNames(marketTeamPersons);

        //get operations team persons
        List<WebElement> opTeamPersons = getTeamMembers("operations");
        Set<String> opNames = findTeamMemberNames(opTeamPersons);

        //get partner-growth team persons
        List<WebElement> partnerTeamPersons = getTeamMembers("partner-growth");
        Set<String> partnerNames = findTeamMemberNames(partnerTeamPersons);

        //get product team persons
        List<WebElement> productTeamPersons = getTeamMembers("product");
        Set<String> productNames = findTeamMemberNames(productTeamPersons);

        //get recruiting team persons
        List<WebElement> recruitTeamPersons = getTeamMembers("recruiting");
        Set<String> recruitNames = findTeamMemberNames(recruitTeamPersons);
	    
	    Set<String> missingPersonInAllPage = new HashSet<>();
	    verifyATeamNamesInAllNames("Data", dataNames, allNames, missingPersonInAllPage);
	    verifyATeamNamesInAllNames("Engineering", engiNames, allNames, missingPersonInAllPage);
	    verifyATeamNamesInAllNames("Marketing", marketNames, allNames, missingPersonInAllPage);
	    verifyATeamNamesInAllNames("Operations", opNames, allNames, missingPersonInAllPage);
	    verifyATeamNamesInAllNames("Partner Growth", partnerNames, allNames, missingPersonInAllPage);
	    verifyATeamNamesInAllNames("Product", productNames, allNames, missingPersonInAllPage);
	    verifyATeamNamesInAllNames("Recruiting", recruitNames, allNames, missingPersonInAllPage);
        assertEquals(missingPersonInAllPage.size(), 0, "There are missing employees in team pages compared to All page. Missing team member is/are "
                               + missingPersonInAllPage);
	}
	
    @Test
    public void testNameInAllTeamPageToEachTeamPage() throws InterruptedException {
        //get all team persons
        List<WebElement> allTeamPersons = getTeamMembers("all");
        Set<String> allNames = findTeamMemberNames(allTeamPersons);        
        
        //get data team persons
        List<WebElement> dataTeamPersons = getTeamMembers("data");
        Set<String> dataNames = findTeamMemberNames(dataTeamPersons);

        //get engineering team persons
        List<WebElement> engiTeamPersons = getTeamMembers("engineering");
        Set<String> engiNames = findTeamMemberNames(engiTeamPersons);

        //get marketing team persons
        List<WebElement> marketTeamPersons = getTeamMembers("marketing");
        Set<String> marketNames = findTeamMemberNames(marketTeamPersons);

        //get operations team persons
        List<WebElement> opTeamPersons = getTeamMembers("operations");
        Set<String> opNames = findTeamMemberNames(opTeamPersons);

        //get partner-growth team persons
        List<WebElement> partnerTeamPersons = getTeamMembers("partner-growth");
        Set<String> partnerNames = findTeamMemberNames(partnerTeamPersons);

        //get product team persons
        List<WebElement> productTeamPersons = getTeamMembers("product");
        Set<String> productNames = findTeamMemberNames(productTeamPersons);

        //get recruiting team persons
        List<WebElement> recruitTeamPersons = getTeamMembers("recruiting");
        Set<String> recruitNames = findTeamMemberNames(recruitTeamPersons);
        
        removeEachTeamNameFromAllTeams(dataNames, allNames);
        removeEachTeamNameFromAllTeams(engiNames, allNames);
        removeEachTeamNameFromAllTeams(marketNames, allNames);
        removeEachTeamNameFromAllTeams(opNames, allNames);
        removeEachTeamNameFromAllTeams(partnerNames, allNames);
        removeEachTeamNameFromAllTeams(productNames, allNames);
        removeEachTeamNameFromAllTeams(recruitNames, allNames);
        assertEquals(allNames.size(), 0, "There are extra employees in all page than the total of each team members. Extra team member in all is/are "
                               + allNames);
    }
    
    @Test
    public void VerifyDeptOfEmployees() throws InterruptedException {
        
        //get all team persons
        List<WebElement> allTeamPersons = getTeamMembers("all");
        Map<String, String> allPersonDept = findPersonDept(allTeamPersons);        
        
        //get data team persons
        List<WebElement> dataTeamPersons = getTeamMembers("data");
        Map<String, String> dataPersonDept = findPersonDept(dataTeamPersons);

        //get engineering team persons
        List<WebElement> engiTeamPersons = getTeamMembers("engineering");
        Map<String, String> engiPersonDept = findPersonDept(engiTeamPersons);

        //get marketing team persons
        List<WebElement> marketTeamPersons = getTeamMembers("marketing");
        Map<String, String> marketPersonDept = findPersonDept(marketTeamPersons);

        //get operations team persons
        List<WebElement> opTeamPersons = getTeamMembers("operations");
        Map<String, String> opPersonDept = findPersonDept(opTeamPersons);

        //get partner-growth team persons
        List<WebElement> partnerTeamPersons = getTeamMembers("partner-growth");
        Map<String, String> partnerPersonDept = findPersonDept(partnerTeamPersons);

        //get product team persons
        List<WebElement> productTeamPersons = getTeamMembers("product");
        Map<String, String> productPersonDept = findPersonDept(productTeamPersons);

        //get recruiting team persons
        List<WebElement> recruitTeamPersons = getTeamMembers("recruiting");
        Map<String, String> recruitPersonDept = findPersonDept(recruitTeamPersons);
        
        Set<String> unMatchedPersonDept = new HashSet<>();
        verifyATeamDept(dataPersonDept, allPersonDept, unMatchedPersonDept);
        verifyATeamDept(engiPersonDept, allPersonDept, unMatchedPersonDept);
        verifyATeamDept(marketPersonDept, allPersonDept, unMatchedPersonDept);
        verifyATeamDept(opPersonDept, allPersonDept, unMatchedPersonDept);
        verifyATeamDept(partnerPersonDept, allPersonDept, unMatchedPersonDept);
        verifyATeamDept(productPersonDept, allPersonDept, unMatchedPersonDept);
        verifyATeamDept(recruitPersonDept, allPersonDept, unMatchedPersonDept);
        assertEquals(unMatchedPersonDept.size(), 0, "There are unmatched dept name in team pages and All page. Unmatched dept name is/are "
                               + unMatchedPersonDept);
    }    
    
    /**
     * delete each employee in each team tag from all employees in all tag.
     * @param aTeamNames
     * @param allNames
     */
    private void removeEachTeamNameFromAllTeams(Set<String> aTeamNames,
            Set<String> allNames) {
        for (String eachName : aTeamNames) {
            if (allNames.contains(eachName)) {
                allNames.remove(eachName);
            }
        }    
    }

    /**
     * get all employee names from team tag page
     * @param teamName
     * @return
     */
    private List<WebElement> getTeamMembers(String teamName) {
        driver.get(teamUrl);
        WebElement teamTagALink = driver.findElement(By.xpath("//a[@rel='" + teamName + "']"));
        wait.until(ExpectedConditions.elementToBeClickable(teamTagALink));
        teamTagALink.click();
		
		List<WebElement> teamPersons = driver.findElements(By.xpath(xpathInlineEmployeesOnPages));
        return teamPersons;
    }
	

    /**
     * verify if employee name in team tag is in the all tag page.
     * @param teamName
     * @param aTeamNames
     * @param allNames
     * @param missingPersonInAllPage
     */
	private void verifyATeamNamesInAllNames(String teamName, Set<String> aTeamNames, 
	        Set<String> allNames, Set<String> missingPersonInAllPage) {
		for (String eachName : aTeamNames) {
		    if (!allNames.contains(eachName)) {
		        missingPersonInAllPage.add(eachName + " in " + teamName +" is not in all page!");
		    }
		}
	}
	
	/**
	 * verify if the dept name of each employee is matched at all tag page.
	 * @param aTeamDept
	 * @param allPersonDept
	 * @param unMatachedDept
	 */
    private void verifyATeamDept(Map<String, String> aTeamDept, Map<String, String> allPersonDept, Set<String> unMatachedDept) {
        for (Map.Entry<String, String> aPersonDept : aTeamDept.entrySet()) {
            String personName = aPersonDept.getKey();
            String personDeptName = aPersonDept.getValue();
            if (allPersonDept.containsKey(personName)) {
                String deptNameInAllPage = allPersonDept.get(personName);
                if (!deptNameInAllPage.equals(personDeptName)) {
                    unMatachedDept.add(personName + " has different dept name in team page and in all page. Team page dept: "
                            + personDeptName + ". All page dept: " + deptNameInAllPage);
                }
            }
        }
    }	
	
    /**
     * get all names of employees from employee webelement list
     * @param teamElements
     * @return
     */
    private Set<String> findTeamMemberNames(List<WebElement> teamElements) {
        Set<String> names = new HashSet<>();
        if (teamElements == null || teamElements.isEmpty()) {
            return names;
        }
        for (WebElement person : teamElements) {
            WebElement personName = person.findElement(By.xpath(".//div[@class='info-block']//h2"));
            names.add(personName.getText());
        }
        return names;
    }   	
    
    /**
     * get employee dept 
     * @param teamElements
     * @return
     */
    private Map<String, String> findPersonDept(List<WebElement> teamElements) {
        Map<String, String> personDepts = new HashMap<>();
        if (teamElements == null || teamElements.isEmpty()) {
            return personDepts;
        }
        for (WebElement person : teamElements) {
            WebElement personName = person.findElement(By.xpath(".//div[@class='info-block']//h2"));
            WebElement personDept = person.findElement(By.xpath(".//div[@class='info-block']//h4"));
            personDepts.put(personName.getText(), personDept.getText());
        }
        return personDepts;
    }      

	/**
	 * find through google search for branch company url and team urls.
	 */
    private void getAllBranchUrls() {
        googleUrl = "http://www.google.com";
        driver.get(googleUrl);
        WebElement search = driver.findElement(By.xpath("//input[@title='Search']"));
        search.sendKeys("Branch");
        
        WebElement confirmButton = driver.findElement(By.name("btnK"));
        wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
        confirmButton.click();

        WebElement branchUrlElement = driver.findElement(By.xpath("//div[@id='rso']//a[@href='https://branch.io/']"));
        branchUrlElement.click();
        wait.until(ExpectedConditions.urlContains("branch.io"));
        branchUrl = driver.getCurrentUrl();
        js.executeScript("window.scrollBy(0, 6000);");
        teamUrl = branchUrl + "team";
        allUrl = branchUrl + "team/#all";

        WebElement team = driver.findElement(By.xpath("//a[contains(text(),'Team')]"));
        wait.until(ExpectedConditions.elementToBeClickable(team));
        team.click();
        wait.until(ExpectedConditions.urlContains("team"));

        WebElement data = driver.findElement(By.xpath("//a[contains(@rel,'data')]"));
        wait.until(ExpectedConditions.elementToBeClickable(data));
        data.click();
        wait.until(ExpectedConditions.urlContains("data"));
        dataUrl = driver.getCurrentUrl();
        
        WebElement engineering = driver.findElement(By.xpath("//a[contains(@rel,'engineering')]"));
        engineering.click();
        wait.until(ExpectedConditions.urlContains("engineering"));
        engineeringUrl = driver.getCurrentUrl();
        
        WebElement marketing = driver.findElement(By.xpath("//a[contains(@rel,'marketing')]"));
        marketing.click();
        wait.until(ExpectedConditions.urlContains("marketing"));
        marketingUrl = driver.getCurrentUrl();
        
        WebElement operations = driver.findElement(By.xpath("//a[contains(@rel,'operations')]"));
        operations.click();
        wait.until(ExpectedConditions.urlContains("operations"));
        operationsUrl = driver.getCurrentUrl();
        
        WebElement partnerGrowth = driver.findElement(By.xpath("//a[contains(@rel,'partner-growth')]"));
        partnerGrowth.click();
        wait.until(ExpectedConditions.urlContains("partner-growth"));
        partnerGrowthUrl = driver.getCurrentUrl();
        
        WebElement product = driver.findElement(By.xpath("//a[contains(@rel,'product')]"));
        product.click();
        wait.until(ExpectedConditions.urlContains("product"));
        productUrl = driver.getCurrentUrl();
        
        WebElement recruiting = driver.findElement(By.xpath("//a[contains(@rel,'recruiting')]"));
        recruiting.click();
        wait.until(ExpectedConditions.urlContains("recruiting"));
        recruitingUrl = driver.getCurrentUrl();

    }	
	
	@AfterClass
	public void afterClass() {
		driver.quit();
	}

}
