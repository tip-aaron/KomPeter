# Git Workflow Guide

This guide explains how we use Git in this project.
Follow these rules to keep our repository clean and avoid conflicts.

## 📊 Workflow Diagram (ASCII)

```text
            +-------------------+
            |       main        |
            |  (production)     |
            +-------------------+
                     ^
                     |
            +-------------------+
            |       dev         |
            | (integration)     |
            +-------------------+
               ^            ^
               |            |
    +----------------+  +----------------+
    | feature/login  |  | feature/cart   |
    +----------------+  +----------------+

Workflow:
1. Create feature branch from `dev`.
2. Work and commit in feature branch.
3. Rebase feature branch with latest `dev`.
4. Merge feature branch back into `dev`.
5. After testing, merge `dev` into `main`.
```

## 1. Branching

- `main` -> stable code (always deployable).
- `dev` -> integration branch for new features.
- Feature branches: `feature/<name>`
  Example: `feature/add-login`
- Bug fixes: `fix/<name>`
  Example: `fix/login-lag`
- Chore branches: `chore/<task>` (config, docs, refactor)
  Example: `chore/update-readme`

## 2. Commit Messages

Follow **Conventional Commits**

- `feat:` → new feature
- `fix:` → bug fix
- `docs:` → documentation changes
- `style:` → code style changes (formatting, no logic changes)
- `refactor:` → code restructuring (no new features, no bug fix)
- `test:` → adding or updating tests
- `chore:` → build, CI, dependencies, etc.

Examples:

feat(inventory): add stock validation on checkout
fix(pos): prevent crash when scanning invalid barcode

## 3. Pull Requests

- Create a pull request to `dev` (not `main`).
- Make sure all tests pass before merging.
- Keep pull requests small and focused on **one feature/fix**.
- Add a clear description of what changed.
- At least **1 approval** required before merging.

## 4. Conflict Resolution

- Sync with `dev` often to reduce conflicts.
- If conflicts happen:

  1. Pull latest `dev`.
  2. Resolve conflicts locally.
  3. Commit with message:

    ```markdown
    fix: resolve merge conflicts with dev
    ```

## 5. Committing Changes

Before committing your changes, make sure that:

1. Code follows our [CODING STANDARD](./CODING_STANDARD.md)
2. No debug code exists like `System.out.printlnt` or commented-out blocks.

## 6. Syncing with Dev

Before starting work:

```bash
git checkout dev
git pull origin dev
git checkout -b feature/my-feature
```

## 7. Use Rebase and Merge

Prefer using **Rebase + Merge** instead of normal merges to keep history clean (no "merge commits").

### Example

1. Start from `dev`:
2. Do your work, commit changes.
3. Before pushing, rebase onto latest `dev`:

    ```bash
    git fetch origin
    git rebase origin/dev
    ```

    If conflicts appear:

    - Fix the files manually.
    - Run:

    ```bash
    git add <file>
    git rebase --continue
    ```

4. Push your branch:

```bash
git push origin feature/add-login
```

## 🧹 8. Cleaning Up

- After merging a PR → delete the feature branch.
- Don’t commit directly to `main` or `dev`.
- Use `.gitignore` to avoid committing IDE files, build outputs, or secrets.

## 🚀 9. Extra Tips

- Commit often, but keep them **small and meaningful**.
- Write messages for **humans**, not just Git.
- Don’t rewrite history on `main` or `dev`.
- If stuck, ask before pushing broken code.
