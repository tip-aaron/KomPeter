# Branch Commands Tips

Here are some useful Git commands for working with branches.

---

## 📌 Checking Branches

- See all branches:

```bash
git branch
```

- See all branches (including remote):

```bash
git branch -a
```

- See which branch you are on:

```bash
git status
```

## 🌱 Creating Branches

- Create and switch to a new branch:

```bash
git checkout -b feature/my-feature
```

- Create a branch without switching:

```bash
git branch feature/my-feature
```

## 🔄 Switching Branches

- Switch to an existing branch:

```bash
git checkout dev
```

or

```bash
git switch dev
```

## ⬆️ Syncing with Remote

- Update your branch list:

```bash
git fetch --all
```

- Update your current branch with remote:

```bash
git pull origin dev
```

## 🔀 Rebasing

- Rebase onto latest dev:

```bash
git fetch origin
git rebase origin/dev
```

- Continue after resolving conflicts:

```bash
git rebase --continue
```

- Cancel a rebase:

```bash
git rebase --abort
```

## 🌐 Pushing Branches

- Push a new branch to remote:

```bash
git push -u origin feature/my-feature
```

- Push updates to an existing branch:

```bash
git push
```

## 🧹 Deleting Branches

- Delete branch locally:

```bash
  git branch -d feature/my-feature
```

- Force delete (if not merged):

```bash
git branch -D feature/my-feature
```

- Delete branch on remote:

```bash
git push origin --delete feature/my-feature
```

## 🕵️ Checking History

- See commit history:

```bash
git log --oneline --graph --decorate --all
```

- See commits on your branch only:

```bash
git log --oneline
```

## ⚠️ Common Mistakes to Avoid

1. **Forgetting to pull/rebase before pushing**
   → Always update your branch with the latest `dev` before pushing.

2. **Committing directly to `main` or `dev`**
   → Always create a feature branch. Don’t work on shared branches.

3. **Forgetting to set upstream when pushing a new branch**
   → Use `git push -u origin <branch>` the first time you push.

4. **Force pushing without care**

   ```bash
    git push -f
   ```

   → This overwrites remote history. Only do this if you’re sure and the branch is not shared.

5. **Merging without pulling first**
   → This creates unnecessary conflicts. Always `git fetch` + `git rebase` before merging.

6. **Leaving stale branches in remote**
   → Delete feature branches after merging to keep the repo clean.

7. **Committing secrets or large files**
   → Double-check what you commit with `git status` before `git add .`.
