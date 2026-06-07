import os
import sys
import subprocess
import json
import urllib.request

MODULES = [
    "core/model",
    "core/database",
    "core/network",
    "feature/quiz",
    "feature/leaderboard"
]

def check_git_repository():
    """Validates if the current directory is a valid Git repository."""
    if not os.path.exists(".git"):
        print("❌ Error: This directory is not a Git repository!")
        print("💡 Please run the 'git init' and 'git remote add' commands first.")
        sys.exit(1)

def create_modules():
    """Automates multi-module directory and build script generation."""
    print("🚀 Starting automated multi-module generation...")
    for module in MODULES:
        path = os.path.join(os.getcwd(), module)
        src_path = os.path.join(path, "src/main/java/com/omni/quiz", module.replace("/", "."))
        os.makedirs(src_path, exist_ok=True)

        gradle_file = os.path.join(path, "build.gradle.kts")
        if not os.path.exists(gradle_file):
            with open(gradle_file, "w") as f:
                f.write('plugins {\n    alias(libs.plugins.android.library)\n    alias(libs.plugins.kotlin.android)\n}\n\nandroid {\n    namespace = "com.omni.quiz.' + module.replace("/", ".") + '"\n    compileSdk = 35\n}')
            print(f"✅ Created module and build.gradle.kts for: {module}")

    settings_path = os.path.join(os.getcwd(), "settings.gradle.kts")
    if os.path.exists(settings_path):
        with open(settings_path, "r") as f:
            content = f.read()
        lines_to_add = [f'include(":{m.replace("/", ":")}")' for m in MODULES if f'include(":{m.replace("/", ":")}")' not in content]
        if lines_to_add:
            with open(settings_path, "a") as f:
                f.write("\n" + "\n".join(lines_to_add) + "\n")
            print("✅ settings.gradle.kts updated!")

def generate_ai_commit_message(diff_text):
    """Fetches a structured commit message from Gemini based on git diff."""
    api_key = os.environ.get("GEMINI_API_KEY")
    if not api_key:
        print("⚠️ GEMINI_API_KEY not found in environment. Using fallback message.")
        return "chore: automated architectural update"

    print("🤖 Analyzing changes with Gemini...")
    url = f"https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key={api_key}"

    prompt = (
        "You are an expert Android Git assistant. Analyze the following git diff and write a concise "
        "commit message using the Conventional Commits specification (e.g., 'feat(feature/quiz): add layout', 'chore(root): update gradle'). "
        "Do NOT use markdown format, markdown code blocks, or quotes. Output ONLY the raw text line of the commit message in English.\n\n"
        f"Git Diff:\n{diff_text[:4000]}"
    )

    data = {"contents": [{"parts": [{"text": prompt}]}]}

    try:
        req = urllib.request.Request(
            url,
            data=json.dumps(data).encode("utf-8"),
            headers={"Content-Type": "application/json"}
        )
        with urllib.request.urlopen(req) as response:
            res_data = json.loads(response.read().decode("utf-8"))
            commit_msg = res_data["candidates"][0]["content"]["parts"][0]["text"].strip()
            return commit_msg.replace("`", "").strip()
    except Exception as e:
        print(f"⚠️ AI generation failed ({e}). Using fallback.")
        return "chore: automated architectural update"

def ai_git_commit():
    """Stages all files, automatically generates a commit message via Gemini, and pushes."""
    check_git_repository()

    # 1. Stage all changes
    subprocess.run(["git", "add", "."], check=True)

    # 2. Get the diff using explicit UTF-8 encoding to prevent Windows cp1252 crashes
    try:
        diff_process = subprocess.run(
            ["git", "diff", "--cached"],
            capture_output=True,
            text=True,
            encoding="utf-8",
            check=True
        )
        diff_text = diff_process.stdout
    except Exception as e:
        print(f"⚠️ Failed to read git diff: {e}")
        return

    if not diff_text or not diff_text.strip():
        print("ℹ️ No changes detected. Nothing to commit.")
        return

    # 3. Let Gemini generate the message
    commit_msg = generate_ai_commit_message(diff_text)
    print(f"✨ Gemini generated message: '{commit_msg}'")

    # 4. Commit locally
    subprocess.run(["git", "commit", "-m", commit_msg], check=True)
    print("✅ Local commit successful!")

    # 5. Push automatically (with error catching if remote configuration fails)
    push = input("Push directly to GitHub? (y/n): ")
    if push.lower() == 'y':
        try:
            print("🚀 Pushing to GitHub...")
            subprocess.run(["git", "push", "-u", "origin", "main"], check=True)
            print("🎉 Code is live on GitHub!")
        except subprocess.CalledProcessError:
            print("\n❌ Push failed. Your local commit is safe, but the remote repository URL might be wrong or unauthorized.")
            print("💡 We will fix your GitHub URL connection in the next step.")

if __name__ == "__main__":
    if len(sys.argv) > 1 and sys.argv[1] == "setup-modules":
        create_modules()
    else:
        ai_git_commit()