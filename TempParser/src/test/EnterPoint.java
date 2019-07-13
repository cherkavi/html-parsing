package test;

import java.sql.Connection;
import database.DatabaseDrivers;
import database.StaticConnector;
import database.functions.DatabaseLogger;
import database.functions.DatabaseSaverConnection;
import shop_list.html.parser.engine.EParseState;
import shop_list.html.parser.engine.EngineSettings;
import shop_list.html.parser.engine.IDetectEndOfParsing;
import shop_list.html.parser.engine.IManager;
import shop_list.html.parser.engine.logger.ConsoleLogger;
import shop_list.html.parser.engine.logger.ELoggerLevel;
import shop_list.html.parser.engine.logger.ILogger;
import shop_list.html.parser.engine.multi_page.section_finder.recursive_finder.RecursiveFinder;
import shop_list.html.parser.engine.parser.MozillaAlternativeParser;
import shop_list.html.parser.engine.parser.NekoParser;
import shop_list.html.parser.engine.saver.ConsoleSaver;
import shops.*;

public class EnterPoint implements IDetectEndOfParsing {
	private static void initMySql() {
		// ConnectorSingleton.pathToDatabase="D:\\eclipse_workspace\\TempParser\\database\\SHOP_LIST_PARSE.GDB";
		StaticConnector.driver = DatabaseDrivers.MySQL;
		StaticConnector.url = StaticConnector.driver.getUrl("127.0.0.1",
				"shop_list");
		StaticConnector.userName = "technik";
		StaticConnector.password = "technik";
	}

	private static void initParser() {
		// BasicConfigurator.configure();
		// Logger.getRootLogger().setLevjel(Level.DEBUG);
		EngineSettings.pathToTemp = "C:\\temp\\";
		// кол-во повторов
		NekoParser.repeatAttempt = 1;
		// ParserUtility.debug=true;
		MozillaAlternativeParser.repeatAttempt = 1;
		RecursiveFinder.debug = true;
	}

	public static void main(String[] args) {
		initParser();
		// ---------------------------------------------------------------
		// FIXME
		IManager source = new plazzma_prom_ua();
		// ---------------------------------------------------------------
		boolean isConsole = true;
		// console
		if (isConsole) {
			ILogger logger = new ConsoleLogger();
			logger.setLevel(ELoggerLevel.DEBUG);
			source.setLogger(logger);
			source.setSaver(new ConsoleSaver());
		} else {
			initMySql();
			ILogger logger = new DatabaseLogger(true);
			logger.setLevel(ELoggerLevel.DEBUG);
			source.setLogger(logger);
			source.setSaver(new DatabaseSaverConnection(logger));
			// удалить по указанному номеру магазина все записи во измбежание
			// двойственности
			Integer shopId = source.getShopId();
			System.out.println("Shop Id: " + shopId);
			removeParseRecordByShop(shopId);
		}
		source.start(new EnterPoint());
	}

	private static void removeParseRecordByShop(Integer shopId) {
		Connection connection = null;
		try {
			connection = StaticConnector.getConnectWrap().getConnection();
			String query = "delete from parse_record where parse_record.id_session in (select id from parse_session where parse_session.id_shop="
					+ shopId + ")";
			connection.createStatement().executeUpdate(query);
			System.out.println(query);
			connection.commit();
			System.out.println("Previous data was removed");
		} catch (Exception ex) {
			System.err.println("#removeParseRecordByShop Exception:"
					+ ex.getMessage());
		} finally {
			try {
				connection.close();
			} catch (Exception ex) {
			}
			;
		}
	}

	@Override
	public void endParsing(IManager manager, EParseState parseEndEvent) {
		System.out.println("Page: " + manager.getShopUrlStartPage() + " : "
				+ parseEndEvent.toString());
	}
}
