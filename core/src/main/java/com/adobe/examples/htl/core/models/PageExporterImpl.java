package com.adobe.examples.htl.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables=Resource.class,resourceType="weretail/components/structure/page",adapters=PageExporter.class)
@Exporter(name = "jackson", extensions = "json", selector="pageinfo")
public class PageExporterImpl implements PageExporter{
	
	// you can now call the following url
	// /content/we-retail/us/en/products/women/_jcr_content.pageinfo.json
	//
	
	@Self
	private Resource resource;
	
	@Inject @Named("jcr:title")
	private String title;
	
	@Inject
	private String name;
	
	private String path;
	
	private List<PageExporter> children = new ArrayList<>();

	@PostConstruct
	protected void init() {
		path = resource.getPath();
		name = resource.getParent().getName();
		resource.getParent().listChildren().forEachRemaining(resource -> processChild(resource) );
	}
	
	private void processChild(Resource r) {
		if ("jcr:content".equals(r.getName())) {
			return;
		}
		Resource jcrContent = r.getChild("jcr:content");
		if ( jcrContent != null) {
			children.add(jcrContent.adaptTo(PageExporter.class));
		}
	}
	
	public String getTitle() {
		return title;
	}

	@Override
	public String getPath() {
		return path;
	}

	public String getName() {
		return name;
	}

	@Override
	public List<PageExporter> getChildren() {
		return children;
	}
	
}
