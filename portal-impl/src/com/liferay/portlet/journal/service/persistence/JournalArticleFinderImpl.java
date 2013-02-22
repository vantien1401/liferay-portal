/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.journal.service.persistence;

import com.liferay.portal.kernel.dao.orm.QueryDefinition;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.CalendarUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.security.permission.InlineSQLHelperUtil;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portlet.journal.NoSuchArticleException;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleConstants;
import com.liferay.portlet.journal.model.impl.JournalArticleImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;

import java.sql.Timestamp;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 * @author Connor McKay
 */
public class JournalArticleFinderImpl
	extends BasePersistenceImpl<JournalArticle>
	implements JournalArticleFinder {

	public static final String COUNT_BY_G_F =
		JournalArticleFinder.class.getName() + ".countByG_F";

	public static final String COUNT_BY_G_U_C =
		JournalArticleFinder.class.getName() + ".countByG_U_C";

	public static final String COUNT_BY_G_C_S =
		JournalArticleFinder.class.getName() + ".countByG_C_S";

	public static final String COUNT_BY_C_G_F_C_A_V_T_D_C_T_S_T_D_R =
		JournalArticleFinder.class.getName() +
			".countByC_G_F_C_A_V_T_D_C_T_S_T_D_R";

	public static final String FIND_BY_EXPIRATION_DATE =
		JournalArticleFinder.class.getName() + ".findByExpirationDate";

	public static final String FIND_BY_REVIEW_DATE =
		JournalArticleFinder.class.getName() + ".findByReviewDate";

	public static final String FIND_BY_R_D =
		JournalArticleFinder.class.getName() + ".findByR_D";

	public static final String FIND_BY_G_U_C =
		JournalArticleFinder.class.getName() + ".findByG_U_C";

	public static final String FIND_BY_G_C_S =
		JournalArticleFinder.class.getName() + ".findByG_C_S";

	public static final String FIND_BY_C_G_F_C_A_V_T_D_C_T_S_T_D_R =
		JournalArticleFinder.class.getName() +
			".findByC_G_F_C_A_V_T_D_C_T_S_T_D_R";

	public int countByKeywords(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String keywords, Double version, String type,
			String ddmStructureKey, String ddmTemplateKey, Date displayDateGT,
			Date displayDateLT, int status, Date reviewDate)
		throws SystemException {

		String[] articleIds = null;
		String[] titles = null;
		String[] descriptions = null;
		String[] contents = null;
		String[] ddmStructureKeys = CustomSQLUtil.keywords(
			ddmStructureKey, false);
		String[] ddmTemplateKeys = CustomSQLUtil.keywords(
			ddmTemplateKey, false);
		boolean andOperator = false;

		if (Validator.isNotNull(keywords)) {
			articleIds = CustomSQLUtil.keywords(keywords, false);
			titles = CustomSQLUtil.keywords(keywords);
			descriptions = CustomSQLUtil.keywords(keywords, false);
			contents = CustomSQLUtil.keywords(keywords, false);
		}
		else {
			andOperator = true;
		}

		QueryDefinition queryDefinition = new QueryDefinition(status);

		return doCountByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleIds, version,
			titles, descriptions, contents, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition, false);
	}

	public int countByG_F(
			long groupId, List<Long> folderIds, QueryDefinition queryDefinition)
		throws SystemException {

		return doCountByG_F(groupId, folderIds, queryDefinition, false);
	}

	public int countByG_U_C(
			long groupId, long userId, long classNameId,
			QueryDefinition queryDefinition)
		throws SystemException {

		return doCountByG_U_C(
			groupId, userId, classNameId, queryDefinition, false);
	}

	public int countByG_C_S(
			long groupId, long classNameId, String ddmStructureKey,
			QueryDefinition queryDefinition)
		throws SystemException {

		return doCountByG_C_S(
			groupId, classNameId, ddmStructureKey, queryDefinition, false);
	}

	public int countByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String articleId, Double version, String title,
			String description, String content, String type,
			String ddmStructureKey, String ddmTemplateKey, Date displayDateGT,
			Date displayDateLT, Date reviewDate, boolean andOperator,
			QueryDefinition queryDefinition)
		throws SystemException {

		String[] ddmStructureKeys = CustomSQLUtil.keywords(
			ddmStructureKey, false);
		String[] ddmTemplateKeys = CustomSQLUtil.keywords(
			ddmTemplateKey, false);

		return countByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleId, version,
			title, description, content, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition);
	}

	public int countByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String articleId, Double version, String title,
			String description, String content, String type,
			String[] ddmStructureKeys, String[] ddmTemplateKeys,
			Date displayDateGT, Date displayDateLT, Date reviewDate,
			boolean andOperator, QueryDefinition queryDefinition)
		throws SystemException {

		String[] articleIds = CustomSQLUtil.keywords(articleId, false);
		String[] titles = CustomSQLUtil.keywords(title);
		String[] descriptions = CustomSQLUtil.keywords(description, false);
		String[] contents = CustomSQLUtil.keywords(content, false);

		return countByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleIds, version,
			titles, descriptions, contents, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition);
	}

	public int countByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String[] articleIds, Double version,
			String[] titles, String[] descriptions, String[] contents,
			String type, String[] ddmStructureKeys, String[] ddmTemplateKeys,
			Date displayDateGT, Date displayDateLT, Date reviewDate,
			boolean andOperator, QueryDefinition queryDefinition)
		throws SystemException {

		return doCountByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleIds, version,
			titles, descriptions, contents, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition, false);
	}

	public int filterCountByKeywords(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String keywords, Double version, String type,
			String ddmStructureKey, String ddmTemplateKey, Date displayDateGT,
			Date displayDateLT, int status, Date reviewDate)
		throws SystemException {

		String[] articleIds = null;
		String[] titles = null;
		String[] descriptions = null;
		String[] contents = null;
		String[] ddmStructureKeys = CustomSQLUtil.keywords(
			ddmStructureKey, false);
		String[] ddmTemplateKeys = CustomSQLUtil.keywords(
			ddmTemplateKey, false);
		boolean andOperator = false;

		if (Validator.isNotNull(keywords)) {
			articleIds = CustomSQLUtil.keywords(keywords, false);
			titles = CustomSQLUtil.keywords(keywords);
			descriptions = CustomSQLUtil.keywords(keywords, false);
			contents = CustomSQLUtil.keywords(keywords, false);
		}
		else {
			andOperator = true;
		}

		QueryDefinition queryDefinition = new QueryDefinition(status);

		return doCountByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleIds, version,
			titles, descriptions, contents, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition, true);
	}

	public int filterCountByG_F(
			long groupId, List<Long> folderIds, QueryDefinition queryDefinition)
		throws SystemException {

		return doCountByG_F(groupId, folderIds, queryDefinition, true);
	}

	public int filterCountByG_U_C(
			long groupId, long userId, long classNameId,
			QueryDefinition queryDefinition)
		throws SystemException {

		return doCountByG_U_C(
			groupId, userId, classNameId, queryDefinition, true);
	}

	public int filterCountByG_C_S(
			long groupId, long classNameId, String ddmStructureKey,
			QueryDefinition queryDefinition)
		throws SystemException {

		return doCountByG_C_S(
			groupId, classNameId, ddmStructureKey, queryDefinition, true);
	}

	public int filterCountByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String articleId, Double version, String title,
			String description, String content, String type,
			String ddmStructureKey, String ddmTemplateKey, Date displayDateGT,
			Date displayDateLT, Date reviewDate, boolean andOperator,
			QueryDefinition queryDefinition)
		throws SystemException {

		String[] ddmStructureKeys = CustomSQLUtil.keywords(
			ddmStructureKey, false);
		String[] ddmTemplateKeys = CustomSQLUtil.keywords(
			ddmTemplateKey, false);

		return filterCountByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleId, version,
			title, description, content, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition);
	}

	public int filterCountByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String articleId, Double version, String title,
			String description, String content, String type,
			String[] ddmStructureKeys, String[] ddmTemplateKeys,
			Date displayDateGT, Date displayDateLT, Date reviewDate,
			boolean andOperator, QueryDefinition queryDefinition)
		throws SystemException {

		String[] articleIds = CustomSQLUtil.keywords(articleId, false);
		String[] titles = CustomSQLUtil.keywords(title);
		String[] descriptions = CustomSQLUtil.keywords(description, false);
		String[] contents = CustomSQLUtil.keywords(content, false);

		return filterCountByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleIds, version,
			titles, descriptions, contents, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition);
	}

	public int filterCountByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String[] articleIds, Double version,
			String[] titles, String[] descriptions, String[] contents,
			String type, String[] ddmStructureKeys, String[] ddmTemplateKeys,
			Date displayDateGT, Date displayDateLT, Date reviewDate,
			boolean andOperator, QueryDefinition queryDefinition)
		throws SystemException {

		return doCountByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleIds, version,
			titles, descriptions, contents, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition, true);
	}

	public List<JournalArticle> filterFindByKeywords(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String keywords, Double version, String type,
			String ddmStructureKey, String ddmTemplateKey, Date displayDateGT,
			Date displayDateLT, int status, Date reviewDate, int start, int end,
			OrderByComparator orderByComparator)
		throws SystemException {

		String[] articleIds = null;
		String[] titles = null;
		String[] descriptions = null;
		String[] contents = null;
		String[] ddmStructureKeys = CustomSQLUtil.keywords(
			ddmStructureKey, false);
		String[] ddmTemplateKeys = CustomSQLUtil.keywords(
			ddmTemplateKey, false);
		boolean andOperator = false;

		if (Validator.isNotNull(keywords)) {
			articleIds = CustomSQLUtil.keywords(keywords, false);
			titles = CustomSQLUtil.keywords(keywords);
			descriptions = CustomSQLUtil.keywords(keywords, false);
			contents = CustomSQLUtil.keywords(keywords, false);
		}
		else {
			andOperator = true;
		}

		QueryDefinition queryDefinition = new QueryDefinition(
			status, start, end, orderByComparator);

		return doFindByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleIds, version,
			titles, descriptions, contents, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition, true);
	}

	public List<JournalArticle> filterFindByG_U_C(
			long groupId, long userId, long classNameId,
			QueryDefinition queryDefinition)
		throws SystemException {

		return doFindByG_U_C(
			groupId, userId, classNameId, queryDefinition, true);
	}

	public List<JournalArticle> filterFindByG_C_S(
			long groupId, long classNameId, String ddmStructureKey,
			QueryDefinition queryDefinition)
		throws SystemException {

		return doFindByG_C_S(
			groupId, classNameId, ddmStructureKey, queryDefinition, true);
	}

	public List<JournalArticle> filterFindByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String articleId, Double version, String title,
			String description, String content, String type,
			String ddmStructureKey, String ddmTemplateKey, Date displayDateGT,
			Date displayDateLT, Date reviewDate, boolean andOperator,
			QueryDefinition queryDefinition)
		throws SystemException {

		String[] ddmStructureKeys = CustomSQLUtil.keywords(
			ddmStructureKey, false);
		String[] ddmTemplateKeys = CustomSQLUtil.keywords(
			ddmTemplateKey, false);

		return filterFindByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleId, version,
			title, description, content, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition);
	}

	public List<JournalArticle> filterFindByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String articleId, Double version, String title,
			String description, String content, String type,
			String[] ddmStructureKeys, String[] ddmTemplateKeys,
			Date displayDateGT, Date displayDateLT, Date reviewDate,
			boolean andOperator, QueryDefinition queryDefinition)
		throws SystemException {

		String[] articleIds = CustomSQLUtil.keywords(articleId, false);
		String[] titles = CustomSQLUtil.keywords(title);
		String[] descriptions = CustomSQLUtil.keywords(description, false);
		String[] contents = CustomSQLUtil.keywords(content, false);

		return filterFindByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleIds, version,
			titles, descriptions, contents, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition);
	}

	public List<JournalArticle> filterFindByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String[] articleIds, Double version,
			String[] titles, String[] descriptions, String[] contents,
			String type, String[] ddmStructureKeys, String[] ddmTemplateKeys,
			Date displayDateGT, Date displayDateLT, Date reviewDate,
			boolean andOperator, QueryDefinition queryDefinition)
		throws SystemException {

		return doFindByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleIds, version,
			titles, descriptions, contents, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition, true);
	}

	public List<JournalArticle> findByExpirationDate(
			long classNameId, Date expirationDateLT,
			QueryDefinition queryDefinition)
		throws SystemException {

		Timestamp expirationDateLT_TS = CalendarUtil.getTimestamp(
			expirationDateLT);

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(
				FIND_BY_EXPIRATION_DATE, queryDefinition);

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity(
				JournalArticleImpl.TABLE_NAME, JournalArticleImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(classNameId);
			qPos.add(queryDefinition.getStatus());
			qPos.add(expirationDateLT_TS);

			return q.list(true);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public List<JournalArticle> findByKeywords(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String keywords, Double version, String type,
			String ddmStructureKey, String ddmTemplateKey, Date displayDateGT,
			Date displayDateLT, int status, Date reviewDate, int start, int end,
			OrderByComparator orderByComparator)
		throws SystemException {

		String[] articleIds = null;
		String[] titles = null;
		String[] descriptions = null;
		String[] contents = null;
		String[] ddmStructureKeys = CustomSQLUtil.keywords(
			ddmStructureKey, false);
		String[] ddmTemplateKeys = CustomSQLUtil.keywords(
			ddmTemplateKey, false);
		boolean andOperator = false;

		if (Validator.isNotNull(keywords)) {
			articleIds = CustomSQLUtil.keywords(keywords, false);
			titles = CustomSQLUtil.keywords(keywords);
			descriptions = CustomSQLUtil.keywords(keywords, false);
			contents = CustomSQLUtil.keywords(keywords, false);
		}
		else {
			andOperator = true;
		}

		QueryDefinition queryDefinition = new QueryDefinition(
			status, start, end, orderByComparator);

		return findByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleIds, version,
			titles, descriptions, contents, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition);
	}

	public List<JournalArticle> findByReviewDate(
			long classNameId, Date reviewDateLT, Date reviewDateGT)
		throws SystemException {

		Timestamp reviewDateLT_TS = CalendarUtil.getTimestamp(reviewDateLT);
		Timestamp reviewDateGT_TS = CalendarUtil.getTimestamp(reviewDateGT);

		Session session = null;
		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_BY_REVIEW_DATE);

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity(
				JournalArticleImpl.TABLE_NAME, JournalArticleImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(classNameId);
			qPos.add(reviewDateGT_TS);
			qPos.add(reviewDateLT_TS);

			return q.list(true);
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	public JournalArticle findByR_D(long resourcePrimKey, Date displayDate)
		throws NoSuchArticleException, SystemException {

		Timestamp displayDate_TS = CalendarUtil.getTimestamp(displayDate);

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_BY_R_D);

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity(
				JournalArticleImpl.TABLE_NAME, JournalArticleImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(resourcePrimKey);
			qPos.add(displayDate_TS);

			List<JournalArticle> articles = q.list();

			if (!articles.isEmpty()) {
				return articles.get(0);
			}
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}

		StringBundler sb = new StringBundler(6);

		sb.append("No JournalArticle exists with the key ");
		sb.append("{resourcePrimKey=");
		sb.append(resourcePrimKey);
		sb.append(", displayDate=");
		sb.append(displayDate);
		sb.append("}");

		throw new NoSuchArticleException(sb.toString());
	}

	public List<JournalArticle> findByG_U_C(
			long groupId, long userId, long classNameId,
			QueryDefinition queryDefinition)
		throws SystemException {

		return doFindByG_U_C(
			groupId, userId, classNameId, queryDefinition, false);
	}

	public List<JournalArticle> findByG_C_S(
			long groupId, long classNameId, String ddmStructureKey,
			QueryDefinition queryDefinition)
		throws SystemException {

		return doFindByG_C_S(
			groupId, classNameId, ddmStructureKey, queryDefinition, false);
	}

	public List<JournalArticle> findByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String articleId, Double version, String title,
			String description, String content, String type,
			String ddmStructureKey, String ddmTemplateKey, Date displayDateGT,
			Date displayDateLT, Date reviewDate, boolean andOperator,
			QueryDefinition queryDefinition)
		throws SystemException {

		String[] articleIds = CustomSQLUtil.keywords(articleId, false);
		String[] titles = CustomSQLUtil.keywords(title);
		String[] descriptions = CustomSQLUtil.keywords(description, false);
		String[] contents = CustomSQLUtil.keywords(content, false);
		String[] ddmStructureKeys = CustomSQLUtil.keywords(
			ddmStructureKey, false);
		String[] ddmTemplateKeys = CustomSQLUtil.keywords(
			ddmTemplateKey, false);

		return findByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleIds, version,
			titles, descriptions, contents, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition);
	}

	public List<JournalArticle> findByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String articleId, Double version, String title,
			String description, String content, String type,
			String[] ddmStructureKeys, String[] ddmTemplateKeys,
			Date displayDateGT, Date displayDateLT, Date reviewDate,
			boolean andOperator, QueryDefinition queryDefinition)
		throws SystemException {

		String[] articleIds = CustomSQLUtil.keywords(articleId, false);
		String[] titles = CustomSQLUtil.keywords(title);
		String[] descriptions = CustomSQLUtil.keywords(description, false);
		String[] contents = CustomSQLUtil.keywords(content, false);

		return findByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleIds, version,
			titles, descriptions, contents, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition);
	}

	public List<JournalArticle> findByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String[] articleIds, Double version,
			String[] titles, String[] descriptions, String[] contents,
			String type, String[] ddmStructureKeys, String[] ddmTemplateKeys,
			Date displayDateGT, Date displayDateLT, Date reviewDate,
			boolean andOperator, QueryDefinition queryDefinition)
		throws SystemException {

		return doFindByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			companyId, groupId, folderIds, classNameId, articleIds, version,
			titles, descriptions, contents, type, ddmStructureKeys,
			ddmTemplateKeys, displayDateGT, displayDateLT, reviewDate,
			andOperator, queryDefinition, false);
	}

	protected int doCountByG_F(
			long groupId, List<Long> folderIds, QueryDefinition queryDefinition,
			boolean inlineSQLHelper)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(COUNT_BY_G_F, queryDefinition);

			if (inlineSQLHelper) {
				sql = InlineSQLHelperUtil.replacePermissionCheck(
					sql, JournalArticle.class.getName(),
					"JournalArticle.resourcePrimKey", groupId);
			}

			sql = StringUtil.replace(
				sql, "[$FOLDER_ID$]",
				getFolderIds(folderIds, JournalArticleImpl.TABLE_NAME));

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);
			qPos.add(queryDefinition.getStatus());

			for (int i = 0; i < folderIds.size(); i++) {
				Long folderId = folderIds.get(i);

				qPos.add(folderId);
			}

			Iterator<Long> itr = q.iterate();

			if (itr.hasNext()) {
				Long count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected int doCountByG_U_C(
			long groupId, long userId, long classNameId,
			QueryDefinition queryDefinition, boolean inlineSQLHelper)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(COUNT_BY_G_U_C, queryDefinition);

			if (inlineSQLHelper) {
				sql = InlineSQLHelperUtil.replacePermissionCheck(
					sql, JournalArticle.class.getName(),
					"JournalArticle.resourcePrimKey", groupId);
			}

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);
			qPos.add(userId);
			qPos.add(classNameId);
			qPos.add(queryDefinition.getStatus());

			Iterator<Long> itr = q.iterate();

			if (itr.hasNext()) {
				Long count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected int doCountByG_C_S(
			long groupId, long classNameId, String ddmStructureKey,
			QueryDefinition queryDefinition, boolean inlineSQLHelper)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(COUNT_BY_G_C_S, queryDefinition);

			if (ddmStructureKey.equals(
					String.valueOf(
						JournalArticleConstants.CLASSNAME_ID_DEFAULT))) {

				sql = StringUtil.replace(
					sql, "(structureId = ?)",
					"((structureId = ?) OR (structureId = '') OR " +
						"(structureId = null))");
			}

			if (inlineSQLHelper) {
				sql = InlineSQLHelperUtil.replacePermissionCheck(
					sql, JournalArticle.class.getName(),
					"JournalArticle.resourcePrimKey", groupId);
			}

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);
			qPos.add(classNameId);
			qPos.add(ddmStructureKey);
			qPos.add(queryDefinition.getStatus());

			Iterator<Long> itr = q.iterate();

			if (itr.hasNext()) {
				Long count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected int doCountByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String[] articleIds, Double version,
			String[] titles, String[] descriptions, String[] contents,
			String type, String[] ddmStructureKeys, String[] ddmTemplateKeys,
			Date displayDateGT, Date displayDateLT, Date reviewDate,
			boolean andOperator, QueryDefinition queryDefinition,
			boolean inlineSQLHelper)
		throws SystemException {

		articleIds = CustomSQLUtil.keywords(articleIds, false);
		titles = CustomSQLUtil.keywords(titles);
		descriptions = CustomSQLUtil.keywords(descriptions, false);
		contents = CustomSQLUtil.keywords(contents, false);
		ddmStructureKeys = CustomSQLUtil.keywords(ddmStructureKeys, false);
		ddmTemplateKeys = CustomSQLUtil.keywords(ddmTemplateKeys, false);
		Timestamp displayDateGT_TS = CalendarUtil.getTimestamp(displayDateGT);
		Timestamp displayDateLT_TS = CalendarUtil.getTimestamp(displayDateLT);
		Timestamp reviewDate_TS = CalendarUtil.getTimestamp(reviewDate);

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(
				COUNT_BY_C_G_F_C_A_V_T_D_C_T_S_T_D_R, queryDefinition);

			if (groupId <= 0) {
				sql = StringUtil.replace(
					sql, "(groupId = ?) AND", StringPool.BLANK);
			}

			if (folderIds.isEmpty()) {
				sql = StringUtil.replace(
					sql, "([$FOLDER_ID$]) AND", StringPool.BLANK);
			}
			else {
				sql = StringUtil.replace(
					sql, "[$FOLDER_ID$]",
					getFolderIds(folderIds, JournalArticleImpl.TABLE_NAME));
			}

			sql = CustomSQLUtil.replaceKeywords(
				sql, "articleId", StringPool.LIKE, false, articleIds);

			if ((version == null) || (version <= 0)) {
				sql = StringUtil.replace(
					sql, "(version = ?) [$AND_OR_CONNECTOR$]",
					StringPool.BLANK);
			}

			sql = CustomSQLUtil.replaceKeywords(
				sql, "lower(title)", StringPool.LIKE, false, titles);
			sql = CustomSQLUtil.replaceKeywords(
				sql, "description", StringPool.LIKE, false, descriptions);
			sql = CustomSQLUtil.replaceKeywords(
				sql, "content", StringPool.LIKE, false, contents);

			if (Validator.isNull(type)) {
				sql = StringUtil.replace(sql, _TYPE_SQL, StringPool.BLANK);
			}

			if (isNullArray(ddmStructureKeys)) {
				sql = StringUtil.replace(
					sql, _STRUCTURE_ID_SQL, StringPool.BLANK);
			}
			else {
				sql = CustomSQLUtil.replaceKeywords(
					sql, "structureId", StringPool.LIKE, false,
					ddmStructureKeys);
			}

			if (isNullArray(ddmTemplateKeys)) {
				sql = StringUtil.replace(
					sql, _TEMPLATE_ID_SQL, StringPool.BLANK);
			}
			else {
				sql = CustomSQLUtil.replaceKeywords(
					sql, "templateId", StringPool.LIKE, false, ddmTemplateKeys);
			}

			sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);

			if (inlineSQLHelper) {
				sql = InlineSQLHelperUtil.replacePermissionCheck(
					sql, JournalArticle.class.getName(),
					"JournalArticle.resourcePrimKey", groupId);

				sql = StringUtil.replace(
					sql, "(companyId", "(JournalArticle.companyId");
			}

			SQLQuery q = session.createSQLQuery(sql);

			q.addScalar(COUNT_COLUMN_NAME, Type.LONG);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(companyId);

			if (groupId > 0) {
				qPos.add(groupId);
			}

			for (long folderId : folderIds) {
				qPos.add(folderId);
			}

			qPos.add(classNameId);
			qPos.add(queryDefinition.getStatus());
			qPos.add(articleIds, 2);

			if ((version != null) && (version > 0)) {
				qPos.add(version);
			}

			qPos.add(titles, 2);
			qPos.add(descriptions, 2);
			qPos.add(contents, 2);
			qPos.add(displayDateGT_TS);
			qPos.add(displayDateGT_TS);
			qPos.add(displayDateLT_TS);
			qPos.add(displayDateLT_TS);
			qPos.add(reviewDate_TS);
			qPos.add(reviewDate_TS);

			if (Validator.isNotNull(type)) {
				qPos.add(type);
				qPos.add(type);
			}

			if (!isNullArray(ddmStructureKeys)) {
				qPos.add(ddmStructureKeys, 2);
			}

			if (!isNullArray(ddmTemplateKeys)) {
				qPos.add(ddmTemplateKeys, 2);
			}

			qPos.add(companyId);

			Iterator<Long> itr = q.iterate();

			if (itr.hasNext()) {
				Long count = itr.next();

				if (count != null) {
					return count.intValue();
				}
			}

			return 0;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected List<JournalArticle> doFindByG_C_S(
			long groupId, long classNameId, String ddmStructureKey,
			QueryDefinition queryDefinition, boolean inlineSQLHelper)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_BY_G_C_S, queryDefinition);

			sql = CustomSQLUtil.replaceOrderBy(
				sql, queryDefinition.getOrderByComparator());

			if (ddmStructureKey.equals(
					String.valueOf(
						JournalArticleConstants.CLASSNAME_ID_DEFAULT))) {

				sql = StringUtil.replace(
					sql, "(structureId = ?)",
					"((structureId = ?) OR (structureId = '') OR" +
						"(structureId = null))");
			}

			if (inlineSQLHelper) {
				sql = InlineSQLHelperUtil.replacePermissionCheck(
					sql, JournalArticle.class.getName(),
					"JournalArticle.resourcePrimKey", groupId);
			}

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity(
				JournalArticleImpl.TABLE_NAME, JournalArticleImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);
			qPos.add(classNameId);
			qPos.add(ddmStructureKey);
			qPos.add(queryDefinition.getStatus());

			return (List<JournalArticle>)QueryUtil.list(
				q, getDialect(), queryDefinition.getStart(),
				queryDefinition.getEnd());
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected List<JournalArticle> doFindByG_U_C(
			long groupId, long userId, long classNameId,
			QueryDefinition queryDefinition, boolean inlineSQLHelper)
		throws SystemException {

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(FIND_BY_G_U_C, queryDefinition);

			sql = CustomSQLUtil.replaceOrderBy(
				sql, queryDefinition.getOrderByComparator());

			if (inlineSQLHelper) {
				sql = InlineSQLHelperUtil.replacePermissionCheck(
					sql, JournalArticle.class.getName(),
					"JournalArticle.resourcePrimKey", groupId);
			}

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity(
				JournalArticleImpl.TABLE_NAME, JournalArticleImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(groupId);
			qPos.add(classNameId);
			qPos.add(userId);
			qPos.add(queryDefinition.getStatus());

			return (List<JournalArticle>)QueryUtil.list(
				q, getDialect(), queryDefinition.getStart(),
				queryDefinition.getEnd());
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected List<JournalArticle> doFindByC_G_F_C_A_V_T_D_C_T_S_T_D_R(
			long companyId, long groupId, List<Long> folderIds,
			long classNameId, String[] articleIds, Double version,
			String[] titles, String[] descriptions, String[] contents,
			String type, String[] ddmStructureKeys, String[] ddmTemplateKeys,
			Date displayDateGT, Date displayDateLT, Date reviewDate,
			boolean andOperator, QueryDefinition queryDefinition,
			boolean inlineSQLHelper)
		throws SystemException {

		articleIds = CustomSQLUtil.keywords(articleIds, false);
		titles = CustomSQLUtil.keywords(titles);
		descriptions = CustomSQLUtil.keywords(descriptions, false);
		contents = CustomSQLUtil.keywords(contents, false);
		ddmStructureKeys = CustomSQLUtil.keywords(ddmStructureKeys, false);
		ddmTemplateKeys = CustomSQLUtil.keywords(ddmTemplateKeys, false);
		Timestamp displayDateGT_TS = CalendarUtil.getTimestamp(displayDateGT);
		Timestamp displayDateLT_TS = CalendarUtil.getTimestamp(displayDateLT);
		Timestamp reviewDate_TS = CalendarUtil.getTimestamp(reviewDate);

		Session session = null;

		try {
			session = openSession();

			String sql = CustomSQLUtil.get(
				FIND_BY_C_G_F_C_A_V_T_D_C_T_S_T_D_R, queryDefinition);

			if (groupId <= 0) {
				sql = StringUtil.replace(
					sql, "(groupId = ?) AND", StringPool.BLANK);
			}

			if (folderIds.isEmpty()) {
				sql = StringUtil.replace(
					sql, "([$FOLDER_ID$]) AND", StringPool.BLANK);
			}
			else {
				sql = StringUtil.replace(
					sql, "[$FOLDER_ID$]",
					getFolderIds(folderIds, JournalArticleImpl.TABLE_NAME));
			}

			sql = CustomSQLUtil.replaceKeywords(
				sql, "articleId", StringPool.LIKE, false, articleIds);

			if ((version == null) || (version <= 0)) {
				sql = StringUtil.replace(
					sql, "(version = ?) [$AND_OR_CONNECTOR$]",
					StringPool.BLANK);
			}

			sql = CustomSQLUtil.replaceKeywords(
				sql, "lower(title)", StringPool.LIKE, false, titles);
			sql = CustomSQLUtil.replaceKeywords(
				sql, "description", StringPool.LIKE, false, descriptions);
			sql = CustomSQLUtil.replaceKeywords(
				sql, "content", StringPool.LIKE, false, contents);

			if (Validator.isNull(type)) {
				sql = StringUtil.replace(sql, _TYPE_SQL, StringPool.BLANK);
			}

			if (isNullArray(ddmStructureKeys)) {
				sql = StringUtil.replace(
					sql, _STRUCTURE_ID_SQL, StringPool.BLANK);
			}
			else {
				sql = CustomSQLUtil.replaceKeywords(
					sql, "structureId", StringPool.LIKE, false,
					ddmStructureKeys);
			}

			if (isNullArray(ddmTemplateKeys)) {
				sql = StringUtil.replace(
					sql, _TEMPLATE_ID_SQL, StringPool.BLANK);
			}
			else {
				sql = CustomSQLUtil.replaceKeywords(
					sql, "templateId", StringPool.LIKE, false, ddmTemplateKeys);
			}

			sql = CustomSQLUtil.replaceAndOperator(sql, andOperator);
			sql = CustomSQLUtil.replaceOrderBy(
				sql, queryDefinition.getOrderByComparator());

			if (inlineSQLHelper) {
				sql = InlineSQLHelperUtil.replacePermissionCheck(
					sql, JournalArticle.class.getName(),
					"JournalArticle.resourcePrimKey", groupId);

				sql = StringUtil.replace(
					sql, "(companyId", "(JournalArticle.companyId");
			}

			SQLQuery q = session.createSQLQuery(sql);

			q.addEntity(
				JournalArticleImpl.TABLE_NAME, JournalArticleImpl.class);

			QueryPos qPos = QueryPos.getInstance(q);

			qPos.add(companyId);

			if (groupId > 0) {
				qPos.add(groupId);
			}

			for (long folderId : folderIds) {
				qPos.add(folderId);
			}

			qPos.add(classNameId);
			qPos.add(queryDefinition.getStatus());
			qPos.add(articleIds, 2);

			if ((version != null) && (version > 0)) {
				qPos.add(version);
			}

			qPos.add(titles, 2);
			qPos.add(descriptions, 2);
			qPos.add(contents, 2);
			qPos.add(displayDateGT_TS);
			qPos.add(displayDateGT_TS);
			qPos.add(displayDateLT_TS);
			qPos.add(displayDateLT_TS);
			qPos.add(reviewDate_TS);
			qPos.add(reviewDate_TS);

			if (Validator.isNotNull(type)) {
				qPos.add(type);
				qPos.add(type);
			}

			if (!isNullArray(ddmStructureKeys)) {
				qPos.add(ddmStructureKeys, 2);
			}

			if (!isNullArray(ddmTemplateKeys)) {
				qPos.add(ddmTemplateKeys, 2);
			}

			qPos.add(companyId);

			return (List<JournalArticle>)QueryUtil.list(
				q, getDialect(), queryDefinition.getStart(),
				queryDefinition.getEnd());
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected String getFolderIds(List<Long> folderIds, String tableName) {
		if (folderIds.isEmpty()) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(folderIds.size() * 2 + 1);

		sb.append(StringPool.OPEN_PARENTHESIS);

		for (int i = 0; i < folderIds.size(); i++) {
			sb.append(tableName);
			sb.append(".folderId = ? ");

			if ((i + 1) != folderIds.size()) {
				sb.append(WHERE_OR);
			}
		}

		sb.append(StringPool.CLOSE_PARENTHESIS);

		return sb.toString();
	}

	protected JournalArticle getLatestArticle(
			long groupId, String articleId, int status)
		throws SystemException {

		List<JournalArticle> articles = null;

		if (status == WorkflowConstants.STATUS_ANY) {
			articles = JournalArticleUtil.findByG_A(groupId, articleId, 0, 1);
		}
		else {
			articles = JournalArticleUtil.findByG_A_ST(
				groupId, articleId, status, 0, 1);
		}

		if (articles.isEmpty()) {
			return null;
		}

		return articles.get(0);
	}

	protected boolean isNullArray(Object[] array) {
		if ((array == null) || (array.length == 0)) {
			return true;
		}

		for (Object obj : array) {
			if (Validator.isNotNull(obj)) {
				return false;
			}
		}

		return true;
	}

	private static final String _STRUCTURE_ID_SQL =
		"(structureId LIKE ? [$AND_OR_NULL_CHECK$]) [$AND_OR_CONNECTOR$]";

	private static final String _TEMPLATE_ID_SQL =
		"(templateId LIKE ? [$AND_OR_NULL_CHECK$]) [$AND_OR_CONNECTOR$]";

	private static final String _TYPE_SQL =
		"(type_ = ? [$AND_OR_NULL_CHECK$]) [$AND_OR_CONNECTOR$]";

}