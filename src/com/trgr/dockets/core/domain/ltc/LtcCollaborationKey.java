package com.trgr.dockets.core.domain.ltc;

import com.trgr.dockets.core.entity.CodeTableValues.ProductEnum;

public class LtcCollaborationKey 
{
   String ltcCollaborationKey;
   String productCode;
   String requestId;
   String batchId;
   String uberBatchId;
   String subBatchId;
   String collectionName;
   
   public LtcCollaborationKey(String ltcCollaborationKey)
   {
	   this.ltcCollaborationKey = ltcCollaborationKey;
	   decodeCollaborationKey();
   }

   	private void decodeCollaborationKey()
   	{
   		if(this.ltcCollaborationKey!=null && !this.ltcCollaborationKey.isEmpty() && this.ltcCollaborationKey.indexOf("_")>0)
   		{
   			String[] partsOfCollaborationKey = ltcCollaborationKey.split("_");
   			if(partsOfCollaborationKey.length>3)
   			{
   				this.productCode = partsOfCollaborationKey[0];
   				if(!getProductCode().equals(ProductEnum.FBR.name()))
   				{
   					this.requestId = partsOfCollaborationKey[1];
   					this.batchId = partsOfCollaborationKey[2];
   					if(partsOfCollaborationKey[3].equals("N")&& partsOfCollaborationKey.length > 4)
   					{
   						this.collectionName = partsOfCollaborationKey[3]+"_" +partsOfCollaborationKey[4];
   						
   					}
   					else
   					{
   						this.collectionName = partsOfCollaborationKey[3];
   					}
   				}
   				//Note that Prod LMS messages for BKR do not seem to follow the below format
   				else if(getProductCode().equals(ProductEnum.FBR.name()))
   				{
   					if(partsOfCollaborationKey.length==5)
   					{
   						this.batchId = partsOfCollaborationKey[1];
   						this.subBatchId = partsOfCollaborationKey[2];
   						this.collectionName = partsOfCollaborationKey[3]+"_" +partsOfCollaborationKey[4];
   					}
   				}
   					
   			}
   			else if(partsOfCollaborationKey.length>2)
   			{
   				this.productCode = partsOfCollaborationKey[0];
   				if(!getProductCode().equals(ProductEnum.FBR.name()))
   				{
   					this.requestId = partsOfCollaborationKey[1];
   					this.batchId = partsOfCollaborationKey[2];
   				}
   				else if(getProductCode().equals(ProductEnum.FBR.name()))
   				{
   					if(partsOfCollaborationKey.length==3)
   					{
   						this.batchId = partsOfCollaborationKey[1];
   						this.subBatchId = partsOfCollaborationKey[2];
   					}
   				}
   					
   			}
   			else if(partsOfCollaborationKey.length==2)
   			{
   				//Bug 144988. Also initialize request id to use in BKR.
   				String[] reqId = partsOfCollaborationKey[0].split("-");
   				this.productCode = ProductEnum.FBR.name();
   				this.requestId = reqId[0];
				this.batchId = partsOfCollaborationKey[0];
				this.subBatchId = partsOfCollaborationKey[1];
  			}
   		}
   		
   	}
	public String getLtcCollaborationKey() {
		return ltcCollaborationKey;
	}
	
	public void setLtcCollaborationKey(String ltcCollaborationKey) {
		this.ltcCollaborationKey = ltcCollaborationKey;
	}
	
	public String getProductCode() {
		return productCode;
	}
	
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public String getBatchId() {
		return batchId;
	}
	
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	public String getUberBatchId() {
		return uberBatchId;
	}
	
	public void setUberBatchId(String uberBatchId) {
		this.uberBatchId = uberBatchId;
	}
	
	public String getSubBatchId() {
		return subBatchId;
	}
	
	public void setSubBatchId(String subBatchId) {
		this.subBatchId = subBatchId;
	}

	/**
	 * @return the collectionName
	 */
	public String getCollectionName() {
		return collectionName;
	}

	/**
	 * @param collectionName the collectionName to set
	 */
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
	
   
   
}
