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
package org.sonar.issuesreport.tree;

import com.google.common.base.Charsets;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.DecoratorContext;
import org.sonar.api.resources.Directory;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Java;
import org.sonar.api.resources.Method;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Scopes;
import org.sonar.api.scan.filesystem.ModuleFileSystem;

import java.util.Arrays;
import java.util.Collections;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResourceTreeTest {

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();

  private ModuleFileSystem fs;
  private ResourceToFileMapper fileMapper;
  private ResourceTree resourceTree;

  @Before
  public void prepare() {
    fs = mock(ModuleFileSystem.class);
    fileMapper = mock(ResourceToFileMapper.class);
    resourceTree = new ResourceTree(fs, fileMapper);
  }

  @Test
  public void should_execute_on_project() {
    assertThat(resourceTree.shouldExecuteOnProject(mock(Project.class))).isTrue();
  }

  @Test
  public void should_create_resource_tree() throws Exception {

    when(fs.sourceCharset()).thenReturn(Charsets.UTF_8);
    java.io.File ioFile = temp.newFile();
    when(fileMapper.getResourceFile("myProject:com.foo.Bar")).thenReturn(ioFile);

    Resource project = new Project("myProject").setName("My Project").setEffectiveKey("myProject");
    Resource module = new Project("myModule").setName("My Module").setEffectiveKey("myModule");
    Resource dir = new Directory("com.foo").setEffectiveKey("myProject:com.foo");
    Resource file = new File("com.foo.Bar").setEffectiveKey("myProject:com.foo.Bar");
    Resource method = Method.createMethod("com.foo.Bar:main", Java.INSTANCE).setEffectiveKey("myProject:com.foo.Bar:main");

    DecoratorContext methodContext = mock(DecoratorContext.class);
    when(methodContext.getResource()).thenReturn(method);
    when(methodContext.getChildren()).thenReturn(Collections.<DecoratorContext> emptyList());
    resourceTree.decorate(method, methodContext);

    DecoratorContext fileContext = mock(DecoratorContext.class);
    when(fileContext.getResource()).thenReturn(file);
    when(fileContext.getChildren()).thenReturn(Arrays.asList(methodContext));
    resourceTree.decorate(file, fileContext);

    DecoratorContext dirContext = mock(DecoratorContext.class);
    when(dirContext.getResource()).thenReturn(dir);
    when(dirContext.getChildren()).thenReturn(Arrays.asList(fileContext));
    resourceTree.decorate(dir, dirContext);

    DecoratorContext moduleContext = mock(DecoratorContext.class);
    when(moduleContext.getResource()).thenReturn(module);
    when(moduleContext.getChildren()).thenReturn(Arrays.asList(dirContext));
    resourceTree.decorate(module, moduleContext);

    DecoratorContext projectContext = mock(DecoratorContext.class);
    when(projectContext.getResource()).thenReturn(project);
    when(projectContext.getChildren()).thenReturn(Arrays.asList(moduleContext));
    resourceTree.decorate(project, projectContext);

    assertThat(resourceTree.getResource("myProject:com.foo.Bar:main")).isNull();

    ResourceNode resource = resourceTree.getResource("myProject:com.foo.Bar");
    assertThat(resource).isNotNull();
    assertThat(resource.getKey()).isEqualTo("myProject:com.foo.Bar");
    assertThat(resource.getName()).isEqualTo("My Module / com.foo.Bar");
    assertThat(resource.getScope()).isEqualTo(Scopes.FILE);
    assertThat(resource.getEncoding()).isEqualTo(Charsets.UTF_8);
    assertThat(resource.getPath()).isEqualTo(ioFile);

    assertThat(resourceTree.getResource("myProject:com.foo")).isNotNull();
    assertThat(resourceTree.getResource("myProject")).isNotNull();
  }
}
