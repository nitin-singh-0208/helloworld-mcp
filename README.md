# hello-mcp-server

A beginner-friendly [Model Context Protocol (MCP)](https://modelcontextprotocol.io/) server built with **Spring Boot 3** and **Spring AI**. It exposes simple tools over **STDIO** (no HTTP server): the AI client starts this process and talks over stdin/stdout.

**Tools:** `greet`, `get_server_time`, `calculate`, `flip_coin`


## Requirements

- **Java 17** (matches `pom.xml`)
- **Apache Maven** 3.8+

## Compile and package

From the repository root:

```bash
mvn clean package -DskipTests
```

This produces a runnable JAR:

```text
target/hello-mcp-server-0.0.1-SNAPSHOT.jar
```

**Optional checks**

```bash
mvn -q -DskipTests compile    # compile only
mvn test                      # run tests (if present)
```

**Logs:** With STDIO transport, application logs are written to a file (not stdout). By default see `application.yml` — logs go to `/tmp/hello-mcp-server.log` on macOS/Linux.

---

## Configure in Claude Desktop

Claude Desktop reads MCP servers from a JSON config file.

| OS      | Config path |
|---------|-------------|
| **macOS**   | `~/Library/Application Support/Claude/claude_desktop_config.json` |
| **Windows** | `%APPDATA%\Claude\claude_desktop_config.json` |

1. Build the JAR (see [Compile and package](#compile-and-package)).
2. Edit the config and add an `mcpServers` entry. Merge with any existing `mcpServers` keys — do not remove other servers unless you intend to.

Example (replace the JAR path with the **absolute** path on your machine):

```json
{
  "mcpServers": {
    "hello-mcp-server": {
      "command": "java",
      "args": [
        "-jar",
        "/absolute/path/to/hello-mcp-server/target/hello-mcp-server-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

3. **Fully quit and restart** Claude Desktop.
4. In chat, you should see MCP connected (e.g. a tool / connector indicator). Try: “What time is it on the MCP server?” so the model can call `get_server_time`.

If `java` is not on the PATH Claude uses, set `"command"` to the full path of your `java` binary (same idea as the JAR path).

---

## Configure in Cursor Desktop

Cursor loads MCP definitions from **`mcp.json`**.

| Scope   | Location |
|---------|----------|
| **Project** | `.cursor/mcp.json` inside the workspace (only this project) |
| **Global**  | `~/.cursor/mcp.json` (all workspaces) |

Project-level config overrides global when both define the same server name. Cursor supports variables such as `${workspaceFolder}` when `mcp.json` lives under `.cursor/` in that project. See [Model Context Protocol (MCP) — Cursor Docs](https://cursor.com/docs/mcp).

1. Build the JAR (see [Compile and package](#compile-and-package)).
2. Create or edit `.cursor/mcp.json` in **this** repo (recommended) or `~/.cursor/mcp.json` for all projects.
3. Add a **stdio** server entry. Use an **absolute** JAR path in `args`, or `${workspaceFolder}` for a portable project config:

```json
{
  "mcpServers": {
    "hello-mcp-server": {
      "type": "stdio",
      "command": "java",
      "args": [
        "-jar",
        "${workspaceFolder}/target/hello-mcp-server-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

If `${workspaceFolder}` does not resolve as expected, replace it with the full path to the project directory that contains `.cursor/mcp.json`.

4. Reload MCP / restart Cursor if the server does not appear (Cursor: **Settings → Features → Model Context Protocol**, or restart the app).
5. In **Agent** chat, MCP tools should show under available tools when relevant. Default behavior may ask you to approve each tool run.

**Debugging:** **View → Output**, choose **MCP Logs** from the dropdown. If the server fails to start, run the same `java -jar ...` command in a terminal to see errors immediately.

**Windows note:** Global config path is typically `%USERPROFILE%\.cursor\mcp.json`.

---

## Project layout

```text
hello-mcp-server/
├── pom.xml
├── README.md
├── MCP_GUIDE.md
└── src/main/
    ├── java/com/demo/mcp/hello/
    │   ├── HelloMcpServerApplication.java
    │   └── HelloToolService.java
    └── resources/
        ├── application.yml
        └── logback-spring.xml
```