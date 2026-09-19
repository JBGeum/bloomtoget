const PROTECTED = /\bdocs[\\/](learning-log\.md|adr\b)/i;
const ALLOWED_GIT = /^\s*git\s+(add|diff|log|show|status|blame)\b[^;&|>$`\r\n]*$/;

let raw = '';
process.stdin.on('data', chunk => raw += chunk);
process.stdin.on('end', () => {
  const input = JSON.parse(raw).tool_input ?? {};
  const path = input.file_path ?? input.notebook_path ?? '';
  const command = input.command ?? '';
  if (PROTECTED.test(path) || (PROTECTED.test(command) && !ALLOWED_GIT.test(command))) {
    process.stderr.write('docs/learning-log.md와 docs/adr/는 사용자가 직접 쓰는 기록이다. Claude는 Read 도구로 읽기만 한다.\n');
    process.exit(2);
  }
});
