/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.journal.content.web.portlet.asset.provider;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.asset.provider.DisplayInformationProvider;
import com.liferay.portlet.journal.model.JournalArticle;

import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;

/**
 * @author Eudaldo Alonso
*/
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=com_liferay_journal_content_web_portlet_JournalContentPortlet"
	},
	service = DisplayInformationProvider.class
)
public class JournalContentDisplayInformationProvider
	implements DisplayInformationProvider {

	@Override
	public String getClassName() {
		return JournalArticle.class.getName();
	}

	@Override
	public String getClassPK(PortletPreferences portletPreferences) {
		return portletPreferences.getValue("articleId", StringPool.BLANK);
	}

}