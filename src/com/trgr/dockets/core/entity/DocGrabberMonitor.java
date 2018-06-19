/**
 * Copyright 2016: Thomson Reuters Global Resources. All Rights Reserved.
 * Proprietary and Confidential information of TRGR. Disclosure, Use or
 * Reproduction without the written authorization of TRGR is prohibited.
 */
package com.trgr.dockets.core.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DOC_GRABBER_MONITOR", schema="DOCKETS_PUB")
public class DocGrabberMonitor {

	public static String PRODUCT = "product";
	public static String COURT = "court";
	public static String RECEIPT_ID = "receiptId";
	public static String PDF_FILE_NAME = "pdfFileName";
	public static String MERGE_FILE_NAME = "mergeFileName";
	public static String SCRAPE_DATE = "scrapeDate";
	public static String PUBLISH_DATE = "publishDate";
	public static String DOCKET_NUMBER = "docketNumber";
	public static String REQUEST_STATUS = "requestStatus";

	@ManyToOne(targetEntity = Court.class, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "COURT_ID", nullable = true)
	private Court court;

	@ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	private Product product;

	@ManyToOne(targetEntity = Status.class, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "STATUS_ID", nullable = false)
	private Status requestStatus;
	
	@Id
	@Column(name = "RECEIPT_ID")
	private String receiptId;

	@Column(name = "PDF_FILE_NAME")
	private String pdfFileName;

	@Column(name = "MERGE_FILE_NAME")
	private String mergeFileName;

	@Column(name = "DOCKET_NUMBER")
	private String docketNumber;

	@Column(name = "SCRAPE_DATE")
	private Date scrapeDate;

	@Column(name = "PUBLISH_DATE")
	private Date publishDate;
	
	public Court getCourt() {
		return court;
	}

	public void setCourt(Court court) {
		this.court = court;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public String getPdfFileName() {
		return pdfFileName;
	}

	public void setPdfFileName(String pdfFileName) {
		this.pdfFileName = pdfFileName;
	}

	public String getMergeFileName() {
		return mergeFileName;
	}

	public void setMergeFileName(String mergeFileName) {
		this.mergeFileName = mergeFileName;
	}

	public String getDocketNumber() {
		return docketNumber;
	}

	public void setDocketNumber(String docketNumber) {
		this.docketNumber = docketNumber;
	}

	public Status getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(Status requestStatus) {
		this.requestStatus = requestStatus;
	}
	
	public Date getScrapeDate() {
		return scrapeDate;
	}

	public void setScrapeDate(Date scrapeDate) {
		this.scrapeDate = scrapeDate;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((court == null) ? 0 : court.hashCode());
		result = prime * result
				+ ((docketNumber == null) ? 0 : docketNumber.hashCode());
		result = prime * result
				+ ((mergeFileName == null) ? 0 : mergeFileName.hashCode());
		result = prime * result
				+ ((pdfFileName == null) ? 0 : pdfFileName.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result
				+ ((publishDate == null) ? 0 : publishDate.hashCode());
		result = prime * result
				+ ((receiptId == null) ? 0 : receiptId.hashCode());
		result = prime * result
				+ ((scrapeDate == null) ? 0 : scrapeDate.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "DocGrabberMonitor [court=" + court + ", product=" + product
				+ ", receiptId=" + receiptId + ", pdfFileName=" + pdfFileName
				+ ", mergeFileName=" + mergeFileName + ", docketNumber="
				+ docketNumber + ", scrapeDate="
				+ scrapeDate + ", publishDate=" + publishDate + "]";
	}

}
