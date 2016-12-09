/*
 * Copyright (c) 2007 - 2014 www.Abiss.gr
 *
 * This file is part of Calipso, a software platform by www.Abiss.gr.
 *
 * Calipso is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Calipso is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Calipso. If not, see http://www.gnu.org/licenses/agpl.html
 */

define([

        'hbs!template/AppRootView',
        'hbs!template/modal-layout',
        'hbs!template/sidebar',
        'hbs!template/header',
        'hbs!template/header-menuitem',
        'hbs!template/HomeLayout',
        'hbs!template/LoginFormView',
        'hbs!template/UseCaseCardFormView',
        'hbs!template/UseCaseLayout',
        'hbs!template/UserInvitationResults',
        'hbs!template/UseCaseSearchLayout',
        'hbs!template/md-report-layout',
        'hbs!template/notfound',
        'hbs!template/footer',
        'hbs!template/UseCaseGridView',
        'hbs!template/md-report-view',
        'hbs!template/UseCaseFormView',
        'hbs!template/tabbed-layout',
        'hbs!template/SmallPageLayout',
        'hbs!template/userRegistrationSubmitted',
        'hbs!template/userProfile',
        'hbs!template/applayout',
        'hbs!template/loginRegistered',
        'hbs!template/headerNotificationsCollectionView',
        'hbs!template/headerNotificationsItemView',
        'hbs!template/support',
        'hbs!template/templateBasedItemView',
        'hbs!template/templateBasedCollectionView',
        'hbs!template/wizard-layout',
        'hbs!template/wizardTabItem',
        'hbs!template/itemViewTemplate']
    ,

    function (AppRootView,
              modal_layout,
              sidebar,
              header,
              header_menuitem,
              HomeLayout,
              LoginFormView,
              UseCaseCardFormView,
              UseCaseLayout,
              UserInvitationResults,
              UseCaseSearchLayout,
              md_report_layout,
              not_found,
              footer,
              UseCaseGridView,
              md_report_view,
              UseCaseFormView,
              tabbed_layout,
              SmallPageLayout,
              userRegistrationSubmitted,
              userProfile,
              applayout,
              loginRegistered,
              headerNotificationsCollectionView,
              headerNotificationsItemView,
              support,
              templateBasedItemView,
              templateBasedCollectionView,
              wizardLayout,
              wizardTabItem,
              itemViewTemplate) {
        return {
            "AppRootView": AppRootView,
            "modal-layout": modal_layout,
            "sidebar": sidebar,
            "header": header,
            "header-menuitem": header_menuitem,
            "HomeLayout": HomeLayout,
            "LoginFormView": LoginFormView,
            "UseCaseCardFormView": UseCaseCardFormView,
            "UseCaseLayout": UseCaseLayout,
            "UserInvitationResults": UserInvitationResults,
            "UseCaseSearchLayout": UseCaseSearchLayout,
            "md-report-layout": md_report_layout,
            "notfound": not_found,
            "footer": footer,
            "UseCaseGridView": UseCaseGridView,
            "md-report-view": md_report_view,
            "UseCaseFormView": UseCaseFormView,
            "tabbed-layout": tabbed_layout,
            "SmallPageLayout": SmallPageLayout,
            "userRegistrationSubmitted": userRegistrationSubmitted,
            "userProfile": userProfile,
            "applayout": applayout,
            "loginRegistered": loginRegistered,
            "headerNotificationsCollectionView": headerNotificationsCollectionView,
            "headerNotificationsItemView": headerNotificationsItemView,
            'support': support,
            "templateBasedItemView": templateBasedItemView,
            "templateBasedCollectionView": templateBasedCollectionView,
            "wizard-layout": wizardLayout,
            "wizardTabItem": wizardTabItem,
            "itemViewTemplate": itemViewTemplate
        };

    });
