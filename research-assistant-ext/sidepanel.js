document.addEventListener('DOMContentLoaded', () => {
    chrome.storage.local.get(['researchNotes'], function(result) {
        if (result.researchNotes) {
            document.getElementById('notes').value = result.researchNotes;
        }
    });

    document.getElementById('summarizeBtn').addEventListener('click', summarizeText);
    document.getElementById('saveNotesBtn').addEventListener('click', saveNotes);
});

async function summarizeText() {
    try {
        const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
        const results = await chrome.scripting.executeScript({
            target: { tabId: tab.id },
            function: () => window.getSelection().toString()
        });
        if (!results || !results[0] || !results[0].result) {
            showResult('No text selected or script execution failed');
            alert('Please select some text to summarize.');
            return;
        }
        const [{ result }] = results;
        const response = await fetch('http://localhost:8080/api/research/process', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ content: result, operation: 'summarize' })
        });
        if (!response.ok) {
            showResult('Error summarizing text');
            alert('Failed to summarize text. Please try again later.');
            return;
        }
        const text = await response.text();
        showResult(text.replace(/\n/g, '<br>'));
    } catch (e) {
        console.error('Error summarizing text:', e);
        showResult('An error occurred while summarizing.');
    }
}

async function saveNotes() {
    const notes = document.getElementById('notes').value;
    await chrome.storage.local.set({ researchNotes: notes });
    alert('Notes saved successfully!');
}

async function showResult(content) {
    document.getElementById('results').innerHTML = 
        `<div class="result-text">
            <div class="result-content">
                ${content}
            </div>
        </div>`;
}