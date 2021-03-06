package Utils;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

public class FileDownloader {

    private static final Logger LOG = LogManager.getLogger(FileDownloader.class);
    private WebDriver driver;
    private String localDownloadPath = "src\\test\\resources\\Downloads\\";
    private boolean followRedirects = true;
    private boolean mimicWebDriverCookieState = true;
    private int httpStatusOfLastDownloadAttempt = 0;

    public FileDownloader(WebDriver driverObject) {
        this.driver = driverObject;
    }

    /**
     * Specify if the FileDownloader class should follow redirects when trying to download a file
     *
     * @param value
     */
    public void followRedirectsWhenDownloading(boolean value) {
        this.followRedirects = value;
    }

    /**
     * Get the current location that files will be downloaded to.
     *
     * @return The filepath that the file will be downloaded to.
     */
    public String localDownloadPath() {
        return this.localDownloadPath;
    }

    /**
     * Set the path that files will be downloaded to.
     *
     * @param filePath The filepath that the file will be downloaded to.
     */
    public void localDownloadPath(String filePath) {
        this.localDownloadPath = filePath;    }

    /**
     * Download the file specified in the href attribute of a WebElement
     *
     * @param element
     * @return
     * @throws Exception
     */
    public String downloadFile(Object element) throws Exception {
    	if(element instanceof WebElement) {
    	if(((WebElement) element).getAttribute("href") != null) {
    		return downloader(element, "href");
    	}
    	else if(((WebElement) element).getAttribute("src") !=null) {
    		return downloader(element, "src");
    	}
    	}
    	else if(element instanceof String){
    		return downloader(element, null);
    	}
		return null;
    }

    /**
     * Download the image specified in the src attribute of a WebElement
     *
     * @param element
     * @return
     * @throws Exception
     */
    public String downloadImage(WebElement element) throws Exception {
       return downloader(element, "src");
    }

    /**
     * Gets the HTTP status code of the last download file attempt
     *
     * @return
     */
    public int getHTTPStatusOfLastDownloadAttempt() {
        return this.httpStatusOfLastDownloadAttempt;
    }

    /**
     * Mimic the cookie state of WebDriver (Defaults to true)
     * This will enable you to access files that are only available when logged in.
     * If set to false the connection will be made as an anonymouse user
     *
     * @param value
     */
    public void mimicWebDriverCookieState(boolean value) {
        this.mimicWebDriverCookieState = value;
    }

    /**
     * Load in all the cookies WebDriver currently knows about so that we can mimic the browser cookie state
     *
     * @param seleniumCookieSet
     * @return
     */
    private BasicCookieStore mimicCookieState(Set<Cookie> seleniumCookieSet) {
        BasicCookieStore mimicWebDriverCookieStore = new BasicCookieStore();
        for (Cookie seleniumCookie : seleniumCookieSet) {
            BasicClientCookie duplicateCookie = new BasicClientCookie(seleniumCookie.getName(), seleniumCookie.getValue());
            duplicateCookie.setDomain(seleniumCookie.getDomain());
            duplicateCookie.setSecure(seleniumCookie.isSecure());
            duplicateCookie.setExpiryDate(seleniumCookie.getExpiry());
            duplicateCookie.setPath(seleniumCookie.getPath());
            mimicWebDriverCookieStore.addCookie(duplicateCookie);
        }

        return mimicWebDriverCookieStore;
    }

    /**
     * Perform the file/image download.
     *
     * @param element
     * @param attribute
     * @return
     * @throws IOException
     * @throws NullPointerException
     */
    private String downloader(Object element, String attribute) throws IOException, NullPointerException, URISyntaxException {
    	String fileToDownloadLocation;
    	if(attribute !=null) {
        fileToDownloadLocation = ((WebElement) element).getAttribute(attribute);
        if (fileToDownloadLocation.trim().equals("")) throw new NullPointerException("The element you have specified does not link to anything!");
    	System.out.println("0");
        System.out.println(localDownloadPath);
    	}
    	else {
    		fileToDownloadLocation = (String) element;
    	}
        URL fileToDownload = new URL(fileToDownloadLocation);
        System.out.println("1");
        System.out.println(fileToDownload);
        System.out.println(fileToDownload.getFile());
        File downloadedFile = new File(this.localDownloadPath + fileToDownload.getFile().replaceFirst("[/\\?].*[/\\?]", ""));
        System.out.println("2");
        System.out.println(downloadedFile);
        if (downloadedFile.canWrite() == false) downloadedFile.setWritable(true);

        CloseableHttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
        BasicHttpContext localContext = new BasicHttpContext();

        //LOG.info("Mimic WebDriver cookie state: " + this.mimicWebDriverCookieState);
        //if (this.mimicWebDriverCookieState) {
        //    localContext.setAttribute(HttpClientContext.COOKIE_STORE, mimicCookieState(this.driver.manage().getCookies()));
        //}
        HttpGet httpget = new HttpGet(fileToDownload.toURI());
        LOG.info("Sending GET request for: " + httpget.getURI());
        HttpResponse response = client.execute(httpget, localContext);
        this.httpStatusOfLastDownloadAttempt = response.getStatusLine().getStatusCode();
        LOG.info("HTTP GET request status: " + this.httpStatusOfLastDownloadAttempt);
        LOG.info("Downloading file: " + downloadedFile.getName());
        FileUtils.copyInputStreamToFile(response.getEntity().getContent(), downloadedFile);
        response.getEntity().getContent().close();

        String downloadedFileAbsolutePath = downloadedFile.getAbsolutePath();
        LOG.info("File downloaded to '" + downloadedFileAbsolutePath + "'");
        
        System.out.println("3");
        System.out.println(downloadedFileAbsolutePath);
        return downloadedFileAbsolutePath;
    }
    

}
