# ServerMemo

Keep notes that belong to the server, not to a chat log.

## Commands

- `/memo <info>` — append one line to `memo/memo.txt`, created on first use.
- `/memo backup` — rotate the current file into `memo/memo<n>.txt`, starting at `memo0.txt`.

Both files live in the server's working directory, so they outlive world resets and stay easy to grep or back up.

## Requirements

Minecraft 1.21.1, NeoForge 21.1.233. Server side only.

## License

MIT
