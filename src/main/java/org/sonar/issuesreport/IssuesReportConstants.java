/*
 * Sonar :: Issues Report :: Plugin
 * Copyright (C) 2013 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.issuesreport;

public interface IssuesReportConstants {
  String HTML_REPORT_ENABLED_KEY = "sonar.issuesReport.html.enable";
  String HTML_REPORT_SHORT_KEY = "sonar.issuesReport.html.short";
  String HTML_REPORT_LOCATION_KEY = "sonar.issuesReport.html.location";
  String HTML_REPORT_LOCATION_DEFAULT = "issues-report.html";

  String CONSOLE_REPORT_ENABLED_KEY = "sonar.issuesReport.console.enable";
}
