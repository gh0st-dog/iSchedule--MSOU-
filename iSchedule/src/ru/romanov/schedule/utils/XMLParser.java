package ru.romanov.schedule.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.romanov.schedule.adapters.UserAdapter;

public abstract class XMLParser {
	private static long MILISEC_IN_DAY = 1000 * 60 * 60 * 24;
	private static long MILISEC_IN_WEEK = MILISEC_IN_DAY * 7;

	public static String OK = "OK";
	public static String NO_AUTH = "NO_AUTH";
	public static String STATUS = "status";
	public static String TOKEN = "token";
	public static String ITEM = "item";
	public static String ID = "id";
	public static String UPDATE_DT = "update-dt";
	public static String MODE = "mode";
	public static String CHECKED = "checked";
	public static String MODE_ADD = "add";
	public static String MODE_DEL = "del";
	public static String PLACE = "place";
	public static String SUBJECT = "subject";
	public static String ACTS = "activities";
	public static String GROUPS = "groups";
	public static String TIME = "time";
	public static String PERIOD = "period";
	public static String START = "start";
	public static String END = "end";
	public static String REPEAT = "repeat";
	public static String DAY_OF_WEEK = "dow";
	public static String HOURS = "hours";
	public static String EXCLUDES = "excludes";
	public static String INCLUDES = "includes";
	public static String NAME = "name";
	public static String EMAIL ="email";
	public static String PHONE = "phone";
	public static String LOGIN = "login";
	public static String LAST_UPDATE_DT = "last_update_dt";
	public static String REPEAT_MODE_EACH = "each";
	public static String REPEAT_MODE_NONE = "none";
	public static String SHORT_STRING_DATE_FORMAT = "yyyy-MM-dd";
	public static String TIME_DATE_FORMAT = "kkmm";
	public static String MY_TIME_DATE_FORMAT = "kk:mm";
	public static String MY_LONG_DATE_FORMAT = "dd.MM.dd  kk:mm";

	/**
	 * —А–∞–Ј–±–Њ—А –Њ—В–≤–µ—В–∞ –љ–∞ –Ј–∞–њ—А–Њ—Б –∞–≤—В–Њ—А–Є–Ј–∞—Ж–Є–Є
	 * 
	 * @param XMLResponse
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document createDocument() throws ParserConfigurationException {
		DocumentBuilder builder = getBuilder();
		return builder.newDocument();
	}
	
	public static DocumentBuilder getBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory
				.newInstance();
		return factory.newDocumentBuilder();
	}
	
	public static XPathExpression getXPathExpression(String str) {
		XPathExpression expr = null;
		XPathFactory factory = XPathFactory.newInstance();
	    XPath xpath = factory.newXPath();
	    try {
			expr = xpath.compile(str);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return expr;
	}
	
	public static Document domFromString(String XMLString) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = getBuilder();
		InputStream str_stream = null;
		Document dom = null;
		str_stream = new ByteArrayInputStream(XMLString.getBytes("UTF-8"));
		try {
			dom = builder.parse(str_stream);
		} catch (IOException e) {
			throw e;
		} catch (SAXException e) {
			throw e;
		} finally {
			str_stream.close();
		}
		return dom;
	}
	
	public static void parseWithXPath(Document doc, String path, HashMap<String, String> resultMap, boolean depth) throws XPathExpressionException {
		XPathExpression expr = getXPathExpression(path);
		NodeList subTree = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		parseSubTree(subTree, resultMap, depth);
	}
	
	public static void parseSubTree(NodeList subTree, HashMap<String, String> resultMap, boolean depth) {
		for (int i = 0; i < subTree.getLength(); i++) {
			Node currentNode = subTree.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				String name = currentNode.getNodeName();
				String value = currentNode.getTextContent();
				resultMap.put(name, value);
				NodeList st = currentNode.getChildNodes();
				if (st.getLength() != 0 && depth){
					parseSubTree(st, resultMap, depth);
				}
			}
		}
	}
	
	public static String documentToString(Document doc) {
		StringWriter sw = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
		try {
			transformer = tf.newTransformer();
		
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	
			transformer.transform(new DOMSource(doc), new StreamResult(sw));
			   
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sw.toString();
	}
	
	public static Map<String, String> parseResponse(String XMLResponse, String xPath) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(XMLResponse.getBytes("UTF-8"));
			DocumentBuilder builder = getBuilder();
			Document dom = builder.parse(is);
			parseWithXPath(dom, xPath, resultMap, false);
		} finally {
			is.close();
		}
		return resultMap;
	}
	

	/**
	 * —А–∞–Ј–±–Њ—А –Њ—В–≤–µ—В–∞ –љ–∞ –Ј–∞–њ—А–Њ—Б –Є–љ—Д–Њ—А–Љ–∞—Ж–Є–Є –Њ –њ–Њ—Б–ї–µ–і–љ–µ–Љ –Њ–±–љ–Њ–≤–ї–µ–љ–Є–Є
	 * 
	 * @param XMLResponse
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static HashMap<String, String> parseLastUpdateInfoResponse(
			String XMLResponse) throws IOException,
			ParserConfigurationException, SAXException {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			Document dom =domFromString(XMLResponse);
			NodeList domNodes = dom.getChildNodes();
			Node response = domNodes.item(0);
			NodeList responseNodes = response.getChildNodes();
			String status = responseNodes.item(0).getFirstChild()
					.getNodeValue();
			if (status.equals(OK)) {
				NodeList info = responseNodes.item(1).getChildNodes();
				map.put(info.item(0).getNodeName(), info.item(0)
						.getFirstChild().getNodeValue());
				map.put(info.item(1).getNodeName(), info.item(1)
						.getFirstChild().getNodeValue());
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			throw e;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw e;
		} catch (SAXException e) {
			e.printStackTrace();
			throw e;
		} 

		return map;
	}

	/**
	 * —А–∞–Ј–±–Њ—А –Њ—В–≤–µ—В–∞ –љ–∞ –Ј–∞–њ—А–Њ—Б –Њ–±–љ–Њ–≤–ї–µ–љ–Є—П
	 * 
	 * @param XMLResponse
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws ParseException
	 */
	public static MySubjectUpdateManager parseXMLScheduleResponse(
			String XMLResponse) throws IOException,
			ParserConfigurationException, SAXException, ParseException {
		MySubjectUpdateManager manager = null;
		try {
			Document dom = domFromString(XMLResponse);
			NodeList domNodes = dom.getChildNodes();
			Node response = domNodes.item(0);
			NodeList responseNodes = response.getChildNodes();
			if (responseNodes.item(0).getChildNodes().item(0).getNodeValue()
					.equals(OK)) {
				if (responseNodes.getLength() > 1) {
					manager = parseSchedule(responseNodes.item(1)
							.getChildNodes().item(0).getChildNodes());
					manager.setStatus(MySubjectUpdateManager.OK);
				}
			} else {
				// –Ю—И–Є–±–Ї–∞! –°—В–∞—В—Г—Б != –Ю–Ъ
				manager = new MySubjectUpdateManager();
				manager.setStatus(MySubjectUpdateManager.FAIL);
			}
		} catch (UnsupportedEncodingException e) {
			throw e;
		} catch (ParserConfigurationException e) {
			throw e;
		} catch (SAXException e) {
			throw e;
		} catch (ParseException e) {
			throw e;
		}
		return manager;
	}

	/**
	 * –°–ї—Г–∂–µ–±–љ—Л–є –Љ–µ—В–Њ–і. –†–∞–Ј–±–Є—А–∞–µ—В NodeList items –Є–Ј schedule
	 * 
	 * @param shedule
	 * @throws ParseException
	 */
	private static MySubjectUpdateManager parseSchedule(NodeList schedule)
			throws ParseException {
		MySubjectUpdateManager manager = new MySubjectUpdateManager();
		for (int i = 0; i < schedule.getLength(); i++) {
			NodeList sItem = schedule.item(i).getChildNodes();
			String mode = sItem.item(0).getFirstChild().getNodeValue();
			if (mode.equals(MODE_ADD)) {
				MySubject sbj = new MySubject();
				String _id = sItem.item(1).getFirstChild().getNodeValue();
				sbj.setId(_id);
				String updateDate = sItem.item(2).getFirstChild()
						.getNodeValue();
				sbj.setUpdateDate(updateDate);
				String checkedStatus = sItem.item(3).getFirstChild()
						.getNodeValue();
				sbj.setChecked(checkedStatus.equals("true") ? true : false);
				String place = sItem.item(4).getFirstChild().getNodeValue();
				sbj.setPlace(place);
				String subject = sItem.item(5).getFirstChild().getNodeValue();
				sbj.setSubject(subject);
				NodeList period = sItem.item(6).getFirstChild().getChildNodes();
				setTimeToMySubjectObject(period, sbj);
				String groups = sItem.item(7).getFirstChild().getNodeValue();
				sbj.setGroups(groups);
				String acts = sItem.item(8).getFirstChild().getNodeValue();
				sbj.setActs(acts);
				manager.addSubjectToAdd(sbj);
			} else {
				// TODO: –£–і–∞–ї–µ–љ–Є–µ —Н–ї–µ–Љ–µ–љ—В–∞
			}

		}
		return manager;

	}

	/**
	 * —А–∞–Ј–Њ–±—А–∞—В—М –Є —Г—Б—В–∞–љ–Њ–≤–Є—В—М –і–∞—В—Г –≤ —Г–Ї–∞–Ј–∞–љ–љ—Л–є MySubject
	 * 
	 * @param period
	 * @param sbj
	 * @throws ParseException
	 */
	private static void setTimeToMySubjectObject(NodeList period, MySubject sbj)
			throws ParseException {
		int curr_position = 0;
		String repeat = period.item(curr_position++).getFirstChild()
				.getNodeValue();
		sbj.setRepeat(repeat);
		ArrayList<Date> includes = new ArrayList<Date>();
		ArrayList<Date> excludes = new ArrayList<Date>();
		SimpleDateFormat sdf = new SimpleDateFormat(SHORT_STRING_DATE_FORMAT);
		if (!repeat.equals(REPEAT_MODE_NONE)) {
			// repeat
			String startDateString = period.item(curr_position++)
					.getFirstChild().getNodeValue();
			sbj.setStartDate(startDateString);
			String endDateString = period.item(curr_position++).getFirstChild()
					.getNodeValue();
			sbj.setEndDate(endDateString);
			String day_of_week = period.item(curr_position++).getFirstChild()
					.getNodeValue();
			sbj.setDayOfWeek(day_of_week);
			Date startDate = sdf.parse(startDateString);
			Date endDate = sdf.parse(endDateString);
			includes.addAll(datesInPeriod(startDate, endDate,
					getDayOfWeekInteger(day_of_week), repeat));
		}
		// Time
		NodeList time = period.item(curr_position++).getChildNodes();
		String startTime = time.item(0).getFirstChild().getNodeValue();
		String endTime = time.item(1).getFirstChild().getNodeValue();
		SimpleDateFormat timeSDF = new SimpleDateFormat(TIME_DATE_FORMAT);
		SimpleDateFormat myTimeSDF = new SimpleDateFormat(MY_TIME_DATE_FORMAT);
		sbj.setStartTime(myTimeSDF.format(timeSDF.parse(startTime)));
		sbj.setEndTime(myTimeSDF.format(timeSDF.parse(endTime)));
		// includes
		NodeList includesNodeList = period.item(curr_position++)
				.getChildNodes();
		for (int i = 0; i < includesNodeList.getLength(); i++) {
			includes.add(sdf.parse(includesNodeList.item(i).getFirstChild()
					.getNodeValue()));
		}
		// excludes
		NodeList excludesNodeList = period.item(curr_position++)
				.getChildNodes();
		for (int i = 0; i < excludesNodeList.getLength(); i++) {
			excludes.add(sdf.parse(excludesNodeList.item(i).getFirstChild()
					.getNodeValue()));
		}

		for (Date date : includes) {
			sbj.addDate(date, INCLUDES);
		}

		for (Date date : excludes) {
			sbj.addDate(date, EXCLUDES);
		}

	}

	/**
	 * –Я–Њ–ї—Г—З–Є—В—М –Љ–∞—Б—Б–Є–≤ –і–∞—В –Є–Ј –њ–µ—А–Є–Њ–і–∞ —Б —Г–Ї–∞–Ј–∞–љ–љ—Л–Љ –і–љ—С–Љ –љ–µ–і–µ–ї–Є
	 * 
	 * @param startDate
	 * @param endDate
	 * @param dayOfWeek
	 * @return
	 */
	private static ArrayList<Date> datesInPeriod(Date startDate, Date endDate,
			int dayOfWeek, String repeat) {
		ArrayList<Date> dates = new ArrayList<Date>();
		GregorianCalendar myCaned = new GregorianCalendar();
		myCaned.setTime(startDate);
		if (repeat.equals(REPEAT_MODE_EACH)) {
			while (myCaned.get(Calendar.DAY_OF_WEEK) != dayOfWeek)
				myCaned.setTime(new Date(myCaned.getTime().getTime()
						+ MILISEC_IN_DAY));
			while (myCaned.getTime().compareTo(endDate) <= 0) {
				dates.add(myCaned.getTime());
				myCaned.setTime(new Date(myCaned.getTime().getTime()
						+ MILISEC_IN_WEEK));
			}
		}
		return dates;
	}

	/**
	 * –њ–µ—А–µ–≤–Њ–і –Є–Ј —Б—В—А–Њ–Ї–Њ–≤–Њ–≥–Њ –Ј–љ–∞—З–µ–љ–Є—П –і–љ—П –љ–µ–і–µ–ї–Є –≤ –Є–љ—В–Њ–≤—Л–є
	 * 
	 * @param dow
	 * @return
	 */
	public static int getDayOfWeekInteger(String dow) {
		if (dow.equals("Sun"))
			return Calendar.SUNDAY;
		if (dow.equals("Mon"))
			return Calendar.MONDAY;
		if (dow.equals("Tue"))
			return Calendar.TUESDAY;
		if (dow.equals("Wed"))
			return Calendar.WEDNESDAY;
		if (dow.equals("Thu"))
			return Calendar.THURSDAY;
		if (dow.equals("Fri"))
			return Calendar.FRIDAY;
		if (dow.equals("Sat"))
			return Calendar.SATURDAY;

		return -1;
	}

}
