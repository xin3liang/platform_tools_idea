/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.codeInsight.completion

import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase
/**
 * @author peter
 */
class HippieCompletionTest extends LightCodeInsightFixtureTestCase {

  public void testDollars() {
    myFixture.configureByText "a.txt", '''
$some_long_variable_name = Obj::instance();
$some_lon<caret>
'''
    complete()
    myFixture.checkResult '''
$some_long_variable_name = Obj::instance();
$some_long_variable_name<caret>
'''
  }

  public void testFromAnotherFile() {
    myFixture.configureByText "b.txt", '''
$some_long_variable_name = Obj::instance();
'''
    myFixture.configureByText "a.txt", '''
$some_lon<caret>
'''

    complete()
    myFixture.checkResult '''
$some_long_variable_name<caret>
'''
  }

  public void "test no middle matching"() {
    myFixture.configureByText "a.txt", '''
fooExpression
exp<caret>
'''
    complete()
    myFixture.checkResult '''
fooExpression
exp<caret>
'''
  }

  public void "test words from javadoc"() {
    myFixture.configureByText "a.java", '''
/** some comment */
com<caret>
'''
    complete()
    myFixture.checkResult '''
/** some comment */
comment<caret>
'''
  }
  
  public void "test words from line comments"() {
    myFixture.configureByText "a.java", '''
// some comment2
com<caret>
'''
    complete()
    myFixture.checkResult '''
// some comment2
comment2<caret>
'''
  }
  public void "test words from block comments"() {
    myFixture.configureByText "a.java", '''
/* some comment3 */
com<caret>
'''
    complete()
    myFixture.checkResult '''
/* some comment3 */
comment3<caret>
'''
  }

  public void "test complete in string literal"() {
    myFixture.configureByText "a.java", '''
class Foo {
  public Collection<JetFile> allInScope(@NotNull GlobalSearchScope scope) {
    System.out.println("allInSco<caret>: " + scope);
  }
}
'''
    complete()
    myFixture.checkResult '''
class Foo {
  public Collection<JetFile> allInScope(@NotNull GlobalSearchScope scope) {
    System.out.println("allInScope<caret>: " + scope);
  }
}
'''
  }

  private void complete() {
    myFixture.performEditorAction(IdeActions.ACTION_HIPPIE_COMPLETION)
  }
}
