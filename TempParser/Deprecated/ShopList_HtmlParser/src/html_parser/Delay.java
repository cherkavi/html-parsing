package html_parser;

/** объект, который содержит необходимые задержки для чтения данных из сети, чтобы избежать */
public class Delay {
	private int delayReadPage;
	private int delayReadSection;
	
	public Delay(){
	}
	
	/** объект, который содержит необходимые задержки для чтения данных из сети, чтобы избежать */
	public Delay(int delayReadSection, int delayReadPage){
		this.delayReadSection=delayReadSection*1000;
		this.delayReadPage=delayReadPage*1000;
	}

	/**
	 * @return the delayReadPage
	 */
	public int getDelayReadPage() {
		return delayReadPage;
	}

	/**
	 * @param delayReadPage the delayReadPage to set
	 */
	public void setDelayReadPage(int delayReadPage) {
		this.delayReadPage = delayReadPage;
	}

	/**
	 * @return the delayReadSection
	 */
	public int getDelayReadSection() {
		return delayReadSection;
	}

	/**
	 * @param delayReadSection the delayReadSection to set
	 */
	public void setDelayReadSection(int delayReadSection) {
		this.delayReadSection = delayReadSection;
	}
	
}
