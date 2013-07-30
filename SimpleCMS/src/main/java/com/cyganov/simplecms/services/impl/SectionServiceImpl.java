package com.cyganov.simplecms.services.impl;

import com.cyganov.simplecms.dao.ContentDao;
import com.cyganov.simplecms.dao.SectionDao;
import com.cyganov.simplecms.domain.Section;
import com.cyganov.simplecms.services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tsyhanou Siarhei
 * Date: 29.07.13
 * Time: 14:00
 */
@Repository
public class SectionServiceImpl implements SectionService {

	@Autowired
	SectionDao sectionDao;

	@Autowired
	ContentDao contentDao;

	@Override
	public Section getSectionById(String id) {
		return sectionDao.getById(id);
	}

	@Override
	public List<Section> getChildrenByParentId(String id) {
		Section section = sectionDao.getById(id);
		return sectionDao.getChildrenByParent(section);
	}

	@Override
	public void updateSection(Section section, String parentId) {
		Section parent = sectionDao.getById(parentId);
		if (!section.getId().equals("")){
			List<Section> children = getChildrenByParentId(section.getId());
			section.setChildren(children);
		} else {
			section.setId(null);
		}
		section.setParent(parent);

		contentDao.saveOrUpdate(section.getContent());
		sectionDao.saveOrUpdate(section);
	}

	@Override
	public void deleteSectionById(String id) {
		Section section = sectionDao.getById(id);
		Integer contentId = section.getContent().getId();

		sectionDao.deleteById(id);
		contentDao.deleteById(contentId);
	}

	@Override
	public List<Section> getSections() {
		return sectionDao.getRootSections();
	}
}