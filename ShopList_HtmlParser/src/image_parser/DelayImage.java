package image_parser;

/** объект-задержка для чтения очередного изображения */
public class DelayImage {
	private int delayMs;
	/** объект-задержка для чтения очередного изображения */
	public DelayImage(int delayMs){
		this.delayMs=delayMs;
	}
	/** получить задержку на чтение в милисекундах */
	public int getDelayMs(){
		return this.delayMs;
	}
}
