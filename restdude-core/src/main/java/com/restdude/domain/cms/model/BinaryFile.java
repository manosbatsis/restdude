/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.cms.model;


import com.restdude.domain.base.model.AbstractSystemUuidPersistable;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author jdmr
 */
@Entity
@Table(name = "images")
public class BinaryFile extends AbstractSystemUuidPersistable{
    
    private String name;

    @Formula(value = " CONCAT(id, '_thumb.png') ")
    private String thumbnailFilename;

	@Formula(value = " CONCAT(id, '.', file_name_extention) ")
	private String newFilename;

	@Column(name = "file_name_extention")
	private String fileNameExtention;

	@Formula(value = " CONCAT(parent_path, '/', id, '.', file_name_extention) ")
	private String path;

	@Column(name = "parent_path")
	private String parentPath;

	private String contentType;
    @Column(name = "size_")
    private Long size;
    private Long thumbnailSize;
    @Transient
    private String url;
    @Transient
    private String thumbnailUrl;
    @Transient
    private String deleteUrl;
    @Transient
    private String deleteType;
    @Transient
    private List<String> initialPreviewConfig;
    @Transient
    private List<String> initialPreview;
    

	public BinaryFile() {}

	public String getName() {
		return name;
	}

	
//	@Override
//	public String toString() {
//		return "BinaryFile [name=" + name + ", thumbnailFilename="
//				+ thumbnailFilename + ", newFilename=" + newFilename
//				+ ", fileNameExtention=" + fileNameExtention + ", path=" + path
//				+ ", parentPath=" + parentPath + ", contentType=" + contentType
//				+ ", size=" + size + ", thumbnailSize=" + thumbnailSize
//				+ ", url=" + url + ", thumbnailUrl=" + thumbnailUrl
//				+ ", deleteUrl=" + deleteUrl + ", deleteType=" + deleteType
//				+ ", getName()=" + getName() + ", getThumbnailFilename()="
//				+ getThumbnailFilename() + ", getNewFilename()="
//				+ getNewFilename() + ", getFileNameExtention()="
//				+ getFileNameExtention() + ", getPath()=" + getPath()
//				+ ", getParentPath()=" + getParentPath()
//				+ ", getContentType()=" + getContentType() + ", getSize()="
//				+ getSize() + ", getThumbnailSize()=" + getThumbnailSize()
//				+ ", getUrl()=" + getUrl() + ", getThumbnailUrl()="
//				+ getThumbnailUrl() + ", getDeleteUrl()=" + getDeleteUrl()
//				+ ", getDeleteType()=" + getDeleteType() + ", toString()="
//				+ super.toString() + ", getCreatedBy()=" + getCreatedBy()
//				+ ", getCreatedDate()=" + getCreatedDate()
//				+ ", getLastModifiedBy()=" + getLastModifiedBy()
//				+ ", getLastModifiedDate()=" + getLastModifiedDate()
//				+ ", getId()=" + getId() + ", isNew()=" + isNew()
//				+ ", hashCode()="
//				+ hashCode() + ", getClass()=" + getClass() + "]";
//	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbnailFilename() {
		return thumbnailFilename;
	}

	public void setThumbnailFilename(String thumbnailFilename) {
		this.thumbnailFilename = thumbnailFilename;
	}

	public String getNewFilename() {
		return newFilename;
	}

	public void setNewFilename(String newFilename) {
		this.newFilename = newFilename;
	}

	public String getFileNameExtention() {
		return fileNameExtention;
	}

	public void setFileNameExtention(String fileNameExtention) {
		this.fileNameExtention = fileNameExtention;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Long getThumbnailSize() {
		return thumbnailSize;
	}

	public void setThumbnailSize(Long thumbnailSize) {
		this.thumbnailSize = thumbnailSize;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	public String getDeleteType() {
		return deleteType;
	}

	public void setDeleteType(String deleteType) {
		this.deleteType = deleteType;
	}

    public List<String> getInitialPreviewConfig() {
		return initialPreviewConfig;
	}

	public void setInitialPreviewConfig(List<String> initialPreviewConfig) {
		this.initialPreviewConfig = initialPreviewConfig;
	}

	public List<String> getInitialPreview() {
		return initialPreview;
	}

	public void setInitialPreview(List<String> initialPreview) {
		this.initialPreview = initialPreview;
	}

	public boolean addInitialPreview(String initialPreview){
		if(CollectionUtils.isEmpty(this.initialPreview)){
			this.initialPreview = new LinkedList<String>();
		}
		return this.initialPreview.add(initialPreview);
	}
	public boolean addInitialPreviewConfig(String initialPreviewConfig){
		if(CollectionUtils.isEmpty(this.initialPreviewConfig)){
			this.initialPreviewConfig = new LinkedList<String>();
		}
		return this.initialPreviewConfig.add(initialPreviewConfig);
	}


}
