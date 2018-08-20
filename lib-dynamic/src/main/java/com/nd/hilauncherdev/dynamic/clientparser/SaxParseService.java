package com.nd.hilauncherdev.dynamic.clientparser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxParseService extends DefaultHandler {
	private Client client = null;
	private Map<String, ClientWidgetView> widgets = null;
	private String current_widget_type = "";
	private ClientWidgetView widget = null;

	private String preTag = null;

	public Client getClient(InputStream xmlStream) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		SaxParseService handler = new SaxParseService();
		parser.parse(xmlStream, handler);
		return handler.getClient();
	}

	public Client getClient() {
		return client;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if ("client".equals(qName)) {
			client = new Client();
		} else if ("widget_name".equals(qName)) {
			preTag = qName;
		} else if ("widget_pkg_name".equals(qName)) {
			preTag = qName;
		} else if ("widget_version".equals(qName)) {
			preTag = qName;
		} else if ("widget_intercept".equals(qName)) {
			preTag = qName;
		} else if ("widget_switch".equals(qName)) {
			preTag = qName;
		}

		else if ("widget_views".equals(qName)) {
			preTag = qName;
			widgets = new HashMap<String, ClientWidgetView>();
		} else if ("widget_view".equals(qName)) {
			preTag = qName;
			widget = new ClientWidgetView();
			if (attributes != null) {
				current_widget_type = attributes.getValue("type");
				widget.setType(current_widget_type);
				// String str = attributes.getValue("main");
				// if (null != str && !"".equals(str) &&
				// "true".equals(str.trim())) {
				// widget.setMain(true);
				// } else {
				// widget.setMain(false);
				// }
				widget.setPreviewName(attributes.getValue("preview_name"));
			}
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ("client".equals(qName)) {
			preTag = null;
		} else if ("widget_name".equals(qName)) {
			preTag = null;
		} else if ("widget_pkg_name".equals(qName)) {
			preTag = null;
		} else if ("widget_version".equals(qName)) {
			preTag = null;
		} else if ("widget_intercept".equals(qName)) {
			preTag = null;
		} else if ("widget_switch".equals(qName)) {
			preTag = null;
		}

		else if ("widget_views".equals(qName)) {
			client.setPluginWidgetViews(widgets);
			preTag = null;
			widgets = null;
		} else if ("widget_view".equals(qName)) {
			widgets.put(current_widget_type, widget);
			current_widget_type = "";
			widget = null;
			preTag = null;
		}

	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (preTag != null) {
			String content = new String(ch, start, length);
			if ("widget_view".equals(preTag)) {
				widget.setLayoutName(content);
			} else if ("widget_name".equals(preTag)) {
				client.setPluginWidgetName(content);
			} else if ("widget_pkg_name".equals(preTag)) {
				client.setPluginPackageName(content);
			} else if ("widget_intercept".equals(preTag)) {
				if (null != content && "true".equals(content)) {
					client.setPluginGestureIntercepted(true);
				}
			} else if ("widget_switch".equals(preTag)) {
				if (null != content && "true".equals(content)) {
					client.setPluginCanSwitch(true);
				}
			} else if ("widget_version".equals(preTag)) {
				int versionCode = 1;
				try {
					versionCode = Integer.parseInt(content);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					client.setPluginVersion(versionCode);
				}
			}
		}
	}

}