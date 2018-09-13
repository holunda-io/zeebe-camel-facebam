package command

import io.zeebe.camel.api.command.DeployProcessCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class DeployProcessCommandTest {

    @Test
    fun `create from classpath resource`() {
        val cmd = DeployProcessCommand.of("/foo/bar.xml")

        assertThat(cmd.xml).isEqualTo("<hello>World</hello>")
        assertThat(cmd.name).isEqualTo("bar.xml")

    }
}
