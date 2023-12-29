/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2023 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
 *
 * Open Hospital is a free and open source software for healthcare data management.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.isf.utils.sms;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.isf.generaldata.ConfigurationProperties;
import org.isf.sms.providers.gsm.GSMGatewayService;
import org.isf.sms.providers.gsm.GSMParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.PortInUseException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;


/**
 * @author Mwithi
 */
public class SetupGSM extends JFrame implements SerialPortEventListener {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SetupGSM.class);
	private Properties props;
	private CommPortIdentifier portId;
	private SerialPort serialPort;
	private InputStream inputStream;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SetupGSM();
		System.exit(0);
	}

	public SetupGSM() {
		props = ConfigurationProperties.loadPropertiesFile(GSMParameters.FILE_PROPERTIES, LOGGER);

		String model = props.getProperty(GSMGatewayService.SERVICE_NAME + ".gmm");

		Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {

			portId = (CommPortIdentifier) portList.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {

				System.out.println("Port found: " + portId.getName() + ' ' + (portId.getPortType() == CommPortIdentifier.PORT_SERIAL ? "SERIAL" : "PARALLEL"));

				try {
					serialPort = (SerialPort) portId.open("SmsSender", 10);
					serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
					serialPort.addEventListener(this);
					serialPort.notifyOnDataAvailable(true);

					OutputStream outputStream = serialPort.getOutputStream();
					if (outputStream != null) {
						System.out.println("Output stream OK");
					} else {
						System.out.println("Output stream not found");
					}

					inputStream = serialPort.getInputStream();
					byte[] command = model.getBytes();
					outputStream.write(command);

					Thread.sleep(5000);

				} catch (PortInUseException e) {
					LOGGER.error("Port in use.");
				} catch (Exception exception) {
					LOGGER.error("Failed to open port '{}'", portId.getName());
					LOGGER.error(exception.getMessage(), exception);
				} finally {
					serialPort.close();
				}
			}
		}
		System.out.println("End.");
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		SerialPort serialPort = (SerialPort) event.getSource();
		String port = serialPort.getName();
		StringBuilder sb = new StringBuilder();
		byte[] buffer = new byte[1];
		try {
			while (inputStream.available() > 0) {
				int len = inputStream.read(buffer);
				sb.append(new String(buffer));
			}
			String answer = sb.toString();
			if (confirm(port, answer) == JOptionPane.YES_OPTION) {
				save(port);
				System.exit(0);
			}
		} catch (IOException ioException) {
			LOGGER.error(ioException.getMessage(), ioException);
		}
	}

	/**
	 * @param port
	 * @param answer
	 * @return
	 * @throws HeadlessException
	 */
	private int confirm(String port, String answer) throws HeadlessException {
		try {
			int ok = answer.indexOf("OK");
			if (ok > 0) {
				answer = answer.substring(2, ok - 3);
			} else {
				return JOptionPane.NO_OPTION;
			}
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			LOGGER.error("outofbound: '{}'", answer);
		}
		LOGGER.info(answer.trim());

		return JOptionPane.showConfirmDialog(this, "Found modem: " + answer + " on port " + port + "\nConfirm?");
	}

	/**
	 * @param port
	 */
	private void save(String port) {

		FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
			.configure(new Parameters().properties()
				.setFileName(GSMParameters.FILE_PROPERTIES)
				.setThrowExceptionOnMissing(true)
				.setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
				.setIncludesAllowed(false));
		try {
			builder.getConfiguration().setProperty(GSMGatewayService.SERVICE_NAME + ".port", port);
			builder.save();
			LOGGER.info("Port saved in " + GSMParameters.FILE_PROPERTIES);
		} catch (ConfigurationException ce) {
			LOGGER.error(ce.getMessage(), ce);
		}
	}

}
