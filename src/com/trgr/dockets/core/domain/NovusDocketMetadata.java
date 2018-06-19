/**
 * 
 */
package com.trgr.dockets.core.domain;

import com.trgr.dockets.core.entity.PublishingRequest;

/**
 * @author C047166
 *
 */
public class NovusDocketMetadata extends SourceDocketMetadata
{

	private String novusGuid;
	
	public NovusDocketMetadata(PublishingRequest publishingRequest)
	{
		super(publishingRequest);
	}

	public String getNovusGuid()
	{
		return novusGuid;
	}

	public void setNovusGuid(String novusGuid)
	{
		this.novusGuid = novusGuid;
	}

	@Override
	public String toString()
	{
		return "NovusDocketMetadata [novusGuid=" + novusGuid + ", legacyId()=" + getLegacyId() + ", docketNumber()="
				+ getDocketNumber() + ", Product()=" + getProduct() + ", vendorId()=" + getVendorId() + "]";
	}
	
}
