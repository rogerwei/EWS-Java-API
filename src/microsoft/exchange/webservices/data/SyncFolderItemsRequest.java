/**************************************************************************
 * copyright file="SyncFolderItemsRequest.java" company="Microsoft"
 *     Copyright (c) Microsoft Corporation.  All rights reserved.
 * 
 * Defines the SyncFolderItemsRequest class.
 **************************************************************************/
package microsoft.exchange.webservices.data;

import microsoft.exchange.webservices.data.exceptions.ArgumentException;
import microsoft.exchange.webservices.data.exceptions.ServiceVersionException;

/***
 * Represents a SyncFolderItems request.
 */
class SyncFolderItemsRequest extends
		MultiResponseServiceRequest<SyncFolderItemsResponse> {

	/** The property set. */
	private PropertySet propertySet;

	/** The sync folder id. */
	private FolderId syncFolderId;

	/** The sync scope. */
	private SyncFolderItemsScope syncScope;

	/** The sync state. */
	private String syncState;

	/** The ignored item ids. */
	private ItemIdWrapperList ignoredItemIds = new ItemIdWrapperList();

	/** The max changes returned. */
	private int maxChangesReturned = 100;

	/**
	 * * Initializes a new instance of the class.
	 * 
	 * @param service
	 *            the service
	 * @throws Exception 
	 */
	protected SyncFolderItemsRequest(ExchangeService service)
			throws Exception {
		super(service, ServiceErrorHandling.ThrowOnError);
	}

	/**
	 * * Creates service response.
	 * 
	 * @param service
	 *            the service
	 * @param responseIndex
	 *            the response index
	 * @return Service response
	 */
	@Override
	protected SyncFolderItemsResponse createServiceResponse(
			ExchangeService service, int responseIndex) {
		return new SyncFolderItemsResponse(this.getPropertySet());
	}

	/***
	 * Gets the expected response message count.
	 * 
	 * @return Number of expected response messages.
	 */
	@Override
	protected int getExpectedResponseMessageCount() {
		return 1;
	}

	/***
	 * Gets the name of the XML element.
	 * 
	 * @return XML element name
	 */
	@Override
	protected String getXmlElementName() {
		return XmlElementNames.SyncFolderItems;
	}

	/***
	 * Gets the name of the response XML element.
	 * 
	 * @return XML element name
	 */
	@Override
	protected String getResponseXmlElementName() {
		return XmlElementNames.SyncFolderItemsResponse;
	}

	/***
	 * Gets the name of the response message XML element.
	 * 
	 * @return XML element name
	 */
	@Override
	protected String getResponseMessageXmlElementName() {
		return XmlElementNames.SyncFolderItemsResponseMessage;
	}

	/**
	 * * Validates request.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Override
	protected void validate() throws Exception {
		super.validate();
		EwsUtilities.validateParam(this.getPropertySet(), "PropertySet");
		EwsUtilities.validateParam(this.getSyncFolderId(), "SyncFolderId");
		this.getSyncFolderId().validate(
				this.getService().getRequestedServerVersion());

		// SyncFolderItemsScope enum was introduced with Exchange2010. Only
		// value NormalItems is valid with previous server versions.
		if (this.getService().getRequestedServerVersion().compareTo(
				ExchangeVersion.Exchange2010) < 0 &&
				 this.syncScope != SyncFolderItemsScope.NormalItems) {
			throw new ServiceVersionException(String.format(
					Strings.EnumValueIncompatibleWithRequestVersion, this
							.getSyncScope().toString(), this.getSyncScope()
							.name(), ExchangeVersion.Exchange2010));
		}

		// SyncFolderItems can only handle summary properties
		this.getPropertySet()
				.validateForRequest(this, true /* summaryPropertiesOnly */);
	}

	/**
	 * * Writes XML elements.
	 * 
	 * @param writer
	 *            the writer
	 * @throws Exception
	 *             the exception
	 */
	@Override
	protected void writeElementsToXml(EwsServiceXmlWriter writer)
			throws Exception {
		this.getPropertySet().writeToXml(writer, ServiceObjectType.Item);

		writer.writeStartElement(XmlNamespace.Messages,
				XmlElementNames.SyncFolderId);
		this.getSyncFolderId().writeToXml(writer);
		writer.writeEndElement();

		writer.writeElementValue(XmlNamespace.Messages,
				XmlElementNames.SyncState, this.getSyncState());

		this.getIgnoredItemIds().writeToXml(writer, XmlNamespace.Messages,
				XmlElementNames.Ignore);

		writer.writeElementValue(XmlNamespace.Messages,
				XmlElementNames.MaxChangesReturned, this
						.getMaxChangesReturned());

		if (this.getService().getRequestedServerVersion().compareTo(
				ExchangeVersion.Exchange2010) >= 0) {
			writer.writeElementValue(XmlNamespace.Messages,
					XmlElementNames.SyncScope, this.syncScope);
		}
	}

	/***
	 * Gets the request version.
	 * 
	 * @return Earliest Exchange version in which this request is supported.
	 */
	@Override
	protected ExchangeVersion getMinimumRequiredServerVersion() {
		return ExchangeVersion.Exchange2007_SP1;
	}

	/**
	 * * Gets or sets the property set. <value>The property set.</value>
	 * 
	 * @return the property set
	 */
	public PropertySet getPropertySet() {
		return this.propertySet;
	}

	/**
	 * Sets the property set.
	 * 
	 * @param propertySet
	 *            the new property set
	 */
	public void setPropertySet(PropertySet propertySet) {
		this.propertySet = propertySet;
	}

	/**
	 * * Gets the sync folder id. <value>The sync folder id.</value>
	 * 
	 * @return the sync folder id
	 */
	public FolderId getSyncFolderId() {
		return this.syncFolderId;
	}

	/**
	 * Sets the sync folder id.
	 * 
	 * @param syncFolderId
	 *            the new sync folder id
	 */
	public void setSyncFolderId(FolderId syncFolderId) {
		this.syncFolderId = syncFolderId;
	}

	/**
	 * * Gets the scope of the sync. <value>The scope of the
	 * sync.</value>
	 * 
	 * @return the sync scope
	 */
	public SyncFolderItemsScope getSyncScope() {
		return this.syncScope;
	}

	/**
	 * Sets the sync scope.
	 * 
	 * @param syncScope
	 *            the new sync scope
	 */
	public void setSyncScope(SyncFolderItemsScope syncScope) {
		this.syncScope = syncScope;
	}

	/**
	 * * Gets the state of the sync. <value>The state of the
	 * sync.</value>
	 * 
	 * @return the sync state
	 */
	public String getSyncState() {
		return this.syncState;
	}

	/**
	 * Sets the sync state.
	 * 
	 * @param syncState
	 *            the new sync state
	 */
	public void setSyncState(String syncState) {
		this.syncState = syncState;
	}

	/**
	 * * Gets the list of ignored item ids. <value>The ignored item ids.</value>
	 * 
	 * @return the ignored item ids
	 */
	public ItemIdWrapperList getIgnoredItemIds() {
		return this.ignoredItemIds;
	}

	/**
	 * * Gets the maximum number of changes returned by SyncFolderItems.
	 * Values must be between 1 and 512. Default is 100.
	 * 
	 * @return the max changes returned
	 */
	public int getMaxChangesReturned() {

		return this.maxChangesReturned;
	}

	/**
	 * Sets the max changes returned.
	 * 
	 * @param maxChangesReturned
	 *            the new max changes returned
	 * @throws ArgumentException
	 *             the argument exception
	 */
	public void setMaxChangesReturned(int maxChangesReturned)
			throws ArgumentException {
		if (maxChangesReturned >= 1 && maxChangesReturned <= 512) {
			this.maxChangesReturned = maxChangesReturned;
		} else {
			throw new ArgumentException(Strings.MaxChangesMustBeBetween1And512);
		}
	}

}
