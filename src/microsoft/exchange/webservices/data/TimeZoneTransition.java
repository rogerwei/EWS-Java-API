/**************************************************************************
 * copyright file="TimeZoneTransition.java" company="Microsoft"
 *     Copyright (c) Microsoft Corporation.  All rights reserved.
 * 
 * Defines the TimeZoneTransition.java.
 **************************************************************************/
package microsoft.exchange.webservices.data;

import javax.xml.stream.XMLStreamException;

import microsoft.exchange.webservices.data.exceptions.ServiceLocalException;
import microsoft.exchange.webservices.data.exceptions.ServiceXmlSerializationException;

/**
 * Represents the base class for all time zone transitions.
 * 
 */
class TimeZoneTransition extends ComplexProperty {

	/** The Period target. */
	private final String PeriodTarget = "Period";
	
	/** The Group target. */
	private final String GroupTarget = "Group";

	/** The time zone definition. */
	private TimeZoneDefinition timeZoneDefinition;
	
	/** The target period. */
	private TimeZonePeriod targetPeriod;
	
	/** The target group. */
	private TimeZoneTransitionGroup targetGroup;

	/**
	 * Creates a time zone period transition of the appropriate type given an
	 * XML element name.
	 * 
	 * @param timeZoneDefinition
	 *            the time zone definition
	 * @param xmlElementName
	 *            the xml element name
	 * @return A TimeZonePeriodTransition instance.
	 * @throws ServiceLocalException
	 *             the service local exception
	 */
	protected static TimeZoneTransition create(
			TimeZoneDefinition timeZoneDefinition, String xmlElementName)
			throws ServiceLocalException {
		if (xmlElementName.equals(XmlElementNames.AbsoluteDateTransition)) {
			return new AbsoluteDateTransition(timeZoneDefinition);
		} else if (xmlElementName
				.equals(XmlElementNames.AbsoluteDateTransition)) {
			return new AbsoluteDateTransition(timeZoneDefinition);
		} else if (xmlElementName
				.equals(XmlElementNames.RecurringDayTransition)) {
			return new RelativeDayOfMonthTransition(timeZoneDefinition);
		} else if (xmlElementName
				.equals(XmlElementNames.RecurringDateTransition)) {
			return new AbsoluteDayOfMonthTransition(timeZoneDefinition);
		} else if (xmlElementName.equals(XmlElementNames.Transition)) {
			return new TimeZoneTransition(timeZoneDefinition);
		} else {
			throw new ServiceLocalException(String
					.format(Strings.UnknownTimeZonePeriodTransitionType,
							xmlElementName));
		}
	}

	/**
	 * Gets the XML element name associated with the transition.
	 *
	 * @return The XML element name associated with the transition.
	 */
	protected String getXmlElementName() {
		return XmlElementNames.Transition;
	}	

	/**
	 * Tries to read element from XML.The reader.
	 * 
	 * @param reader The
	 *            reader.
	 * @return True if element was read.
	 * @throws Exception the exception
	 */
	@Override
	protected boolean tryReadElementFromXml(EwsServiceXmlReader reader)
			throws Exception {
		if (reader.getLocalName().equals(XmlElementNames.To)) {
			String targetKind = reader
					.readAttributeValue(XmlAttributeNames.Kind);
			String targetId = reader.readElementValue();
			if (targetKind.equals(PeriodTarget)) {
				if (!this.timeZoneDefinition.getPeriods().containsKey(targetId)) {
					this.targetPeriod = this.timeZoneDefinition.getPeriods()
							.get(targetId);
					throw new ServiceLocalException(String.format(
							Strings.PeriodNotFound, targetId));
				}
			} else if (targetKind.equals(GroupTarget)) {
				if (!this.timeZoneDefinition.getTransitionGroups().containsKey(
						targetId)) {
					this.targetGroup = this.timeZoneDefinition
							.getTransitionGroups().get(targetId);
					throw new ServiceLocalException(String.format(
							Strings.TransitionGroupNotFound, targetId));
				}
			} else {
				throw new ServiceLocalException(
						Strings.UnsupportedTimeZonePeriodTransitionTarget);
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Writes elements to XML.
	 * 
	 * @param writer
	 *            the writer
	 * @throws ServiceXmlSerializationException
	 *             the service xml serialization exception
	 * @throws XMLStreamException
	 *             the xML stream exception
	 */
	@Override
	protected void writeElementsToXml(EwsServiceXmlWriter writer)
			throws ServiceXmlSerializationException, XMLStreamException {
		writer.writeStartElement(XmlNamespace.Types, XmlElementNames.To);

		if (this.targetPeriod != null) {
			writer.writeAttributeValue(XmlAttributeNames.Kind, PeriodTarget);
			writer.writeValue(this.targetPeriod.getId(), XmlElementNames.To);
		} else {
			writer.writeAttributeValue(XmlAttributeNames.Kind, GroupTarget);
			writer.writeValue(this.targetGroup.getId(), XmlElementNames.To);
		}

		writer.writeEndElement(); // To
	}

	/**
	 * Loads from XML.
	 * 
	 * @param reader
	 *            the reader
	 * @throws Exception
	 *             the exception
	 */
	protected void loadFromXml(EwsServiceXmlReader reader) throws Exception {
		this.loadFromXml(reader, this.getXmlElementName());
	}

	/**
	 * Writes to XML.
	 * 
	 * @param writer
	 *            the writer
	 * @throws Exception
	 *             the exception
	 */
	protected void writeToXml(EwsServiceXmlWriter writer) throws Exception {
		this.writeToXml(writer, this.getXmlElementName());
	}

	/**
	 * Initializes a new instance of the class.
	 * 
	 * @param timeZoneDefinition
	 *            the time zone definition
	 */
	protected TimeZoneTransition(TimeZoneDefinition timeZoneDefinition) {
		super();
		this.timeZoneDefinition = timeZoneDefinition;
	}

	/**
	 * Initializes a new instance of the class.
	 * 
	 * @param timeZoneDefinition
	 *            the time zone definition
	 * @param targetGroup
	 *            the target group
	 */
	protected TimeZoneTransition(TimeZoneDefinition timeZoneDefinition,
			TimeZoneTransitionGroup targetGroup) {
		this(timeZoneDefinition);
		this.targetGroup = targetGroup;
	}

	/**
	 * Initializes a new instance of the class.
	 * 
	 * @param timeZoneDefinition
	 *            the time zone definition
	 * @param targetPeriod
	 *            the target period
	 */
	protected TimeZoneTransition(TimeZoneDefinition timeZoneDefinition,
			TimeZonePeriod targetPeriod) {
		this(timeZoneDefinition);
		this.targetPeriod = targetPeriod;
	}

	/**
	 * Gets the target period of the transition.
	 *
	 * @return the target period
	 */
	protected TimeZonePeriod getTargetPeriod() {
		return this.targetPeriod;
	}

	/**
	 * Gets the target transition group of the transition.
	 *
	 * @return the target group
	 */
	protected TimeZoneTransitionGroup getTargetGroup() {
		return this.targetGroup;
	}

}
