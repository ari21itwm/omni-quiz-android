import os
import sys
import subprocess
import json
import urllib.request

MODULES = ["core/model", "core/database", "core/network", "feature/quiz", "feature/leaderboard"]

def check_git_repository():
    if not os.path.exists(".git"):
        print("❌ Error: Not a git repository.")
        sys.exit(1)

def generate_ai_commit_message(diff_text):
    api_key = os.environ.get("GEMINI_API_KEY")
    if not api_key:
        print("⚠️ Warning: GEMINI_API_KEY not found in environment. Using fallback message.")
        return "chore: automated architectural update"

    url = f"https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key={api_key}"
    prompt = (
        "You are an expert Android Git assistant. Analyze the following git diff and write a concise "
        "commit message using the Conventional Commits specification (e.g., 'feat(feature/quiz): add layout'). "
        "Do NOT use markdown, quotes, or explanations. Output ONLY the raw text line in English.\n\n"
        f"Git Diff:\n{diff_text[:4000]}"
    )
    data = {"contents": [{"parts": [{"text": prompt}]}]}
    try:
        req = urllib.request.Request(url, data=json.dumps(data).encode("utf-8"), headers={"Content-Type": "application/json"})
        with urllib.request.urlopen(req) as response:
            res_data = json.loads(response.read().decode("utf-8"))
            return res_data["candidates"][0]["content"]["parts"][0]["text"].strip().replace("`", "")
    except Exception as e:
        print(f"⚠️ AI Generation failed ({e}). Using fallback message.")
        return "chore: automated architectural update"

def execute_autonomous_pipeline():
    """Executes staging, verification, commit, and cloud push with optional manual messages."""
    check_git_repository()
    try:
        # 1. Stage changes
        subprocess.run(["git", "add", "."], check=True)

        # 2. Get Diff
        diff_process = subprocess.run(["git", "diff", "--cached"], capture_output=True, text=True, encoding="utf-8", check=True)
        diff_text = diff_process.stdout

        if not diff_text.strip():
            print("ℹ️ No source changes detected. Pipeline skipped.")
            return

        print("✨ Autonomous Pipeline initiated!")

        # 3. Cross-Platform Health Check Build
        gradle_binary = "gradlew.bat" if os.name == "nt" else "./gradlew"
        print(f"🛠️ Compiling project to verify structural integrity via {gradle_binary}...")

        build_res = subprocess.run([gradle_binary, "assembleDebug", "--parallel"], capture_output=True, text=True)
        if build_res.returncode != 0:
            print("❌ Compile Error detected! Postponing commit to prevent pushing broken code.")
            print(build_res.stderr)
            sys.exit(1)

        # 4. Decide between Manual Commit Message or AI
        if len(sys.argv) > 1 and sys.argv[1].strip():
            commit_msg = sys.argv[1].strip()
            print(f"✍️ Using manual commit message: '{commit_msg}'")
        else:
            commit_msg = generate_ai_commit_message(diff_text)
            print(f"🤖 Gemini Message generated: '{commit_msg}'")

        # 5. Commit & Push
        subprocess.run(["git", "commit", "-m", commit_msg], check=True)
        print("🚀 Pushing autonomously to GitHub...")
        
        # Get current branch to push correctly
        branch_res = subprocess.run(["git", "branch", "--show-current"], capture_output=True, text=True, check=True)
        current_branch = branch_res.stdout.strip()
        
        subprocess.run(["git", "push", "origin", current_branch], check=True)
        print("🎉 Cloud Sync Complete! Code is live and verified.")

    except Exception as e:
        print(f"❌ Autonomous pipeline crashed: {e}")
        sys.exit(1)

if __name__ == "__main__":
    execute_autonomous_pipeline()