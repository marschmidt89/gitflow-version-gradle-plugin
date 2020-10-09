/*
 * Copyright 2015 Intershop Communications AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.intershop.gradle.gitflow

import com.intershop.gradle.scm.test.utils.AbstractTaskGroovySpec
import Slf4j
import Rule
import TemporaryFolder
import Unroll

import static TaskOutcome.SUCCESS

@Slf4j
@Unroll
class IntVersionFileGroovySpec extends AbstractTaskGroovySpec {

    @Rule TemporaryFolder temp

    def 'test showVersion task of new project - three digits - #gradleVersion'(gradleVersion) {
        setup:
        def projectDir = temp.newFolder()

        def testFile = new File(projectDir, 'test.properties')
        testFile.text = '''com.test.property = 1
        com.test.value = 1'''

        buildFile << """
        plugins {
            id 'com.intershop.gradle.scmversion'
        }

        version = scm.version.version

        """.stripIndent()

        when:
        def result = getPreparedGradleRunner()
                .withArguments('showVersion', '--stacktrace')
                .withGradleVersion(gradleVersion)
                .build()

        then:
        result.task(":showVersion").outcome == org.gradle.testkit.runner.TaskOutcome.SUCCESS
        result.output.contains('Project version: 1.0.0-SNAPSHOT')

        where:
        gradleVersion << supportedGradleVersions
    }

    def 'test showVersion task of new project - four digits - #gradleVersion'(gradleVersion) {
        setup:
        def projectDir = temp.newFolder()

        def testFile = new File(projectDir, 'test.properties')
        testFile.text = '''com.test.property = 1
        com.test.value = 1'''

        buildFile << """
        plugins {
            id 'com.intershop.gradle.scmversion'
        }

        scm{
            version {
                type = 'threeDigits'
                initialVersion = '1.0.0'
            }
        }

        version = scm.version.version

        """.stripIndent()

        when:
        def result = getPreparedGradleRunner()
                .withArguments('showVersion', '--stacktrace')
                .withGradleVersion(gradleVersion)
                .build()

        then:
        result.task(":showVersion").outcome == org.gradle.testkit.runner.TaskOutcome.SUCCESS
        result.output.contains('Project version: 1.0.0-SNAPSHOT')

        where:
        gradleVersion << supportedGradleVersions
    }
}
