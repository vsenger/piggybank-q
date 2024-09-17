const apiUrl = '/entryResource';

async function createEntry() {
    const entry = getEntryFromForm();
    try {
        const response = await fetch(apiUrl, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(entry)
        });
        const result = await response.json();
        displayResults('Entry created successfully', result);
        clearForm();
    } catch (error) {
        displayResults('Error creating entry', error);
    }
}

async function readEntry() {
    const timestamp = document.getElementById('timestamp').value;
    try {
        const response = await fetch(`${apiUrl}/${encodeURIComponent(timestamp)}`);
        const result = await response.json();
        displayResults('Entry read successfully', result);
    } catch (error) {
        displayResults('Error reading entry', error);
    }
}

async function updateEntry() {
    const entry = getEntryFromForm();
    try {
        const response = await fetch(apiUrl, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(entry)
        });
        const result = await response.json();
        displayResults('Entry updated successfully', result);
    } catch (error) {
        displayResults('Error updating entry', error);
    }
}

async function deleteEntry() {
    const timestamp = document.getElementById('timestamp').value;
    try {
        const response = await fetch(`${apiUrl}/${encodeURIComponent(timestamp)}`, {
            method: 'DELETE'
        });
        const result = await response.json();
        displayResults('Entry deleted successfully', result);
        clearForm();
    } catch (error) {
        displayResults('Error deleting entry', error);
    }
}

async function listEntries() {
    const filters = getFiltersFromForm();
    const queryString = new URLSearchParams(filters).toString();
    try {
        const response = await fetch(`${apiUrl}/findBy?${queryString}`);
        const result = await response.json();
        displayResults('Entries found', result);
    } catch (error) {
        displayResults('Error listing entries', error);
    }
}

function getEntryFromForm() {
    return {
        timestamp: document.getElementById('timestamp').value,
        text: document.getElementById('text').value,
        longNumber: parseInt(document.getElementById('longNumber').value),
        doubleNumber: parseFloat(document.getElementById('doubleNumber').value),
        bool: document.getElementById('bool').checked
    };
}

function getFiltersFromForm() {
    const filters = {};
    const timestamp = document.getElementById('filterTimestamp').value;
    const text = document.getElementById('filterText').value;
    const longNumber = document.getElementById('filterLongNumber').value;
    const doubleNumber = document.getElementById('filterDoubleNumber').value;

    if (timestamp) filters.timestamp = timestamp;
    if (text) filters.text = text;
    if (longNumber) filters.longNumber = longNumber;
    if (doubleNumber) filters.doubleNumber = doubleNumber;

    return filters;
}

function displayResults(message, data) {
    const resultsDiv = document.getElementById('results');
    resultsDiv.innerHTML = `
        <h3>${message}</h3>
        <pre>${JSON.stringify(data, null, 2)}</pre>
    `;
}

function clearForm() {
    document.getElementById('entry-form').reset();
}

// Add event listeners for form submissions
document.getElementById('entry-form').addEventListener('submit', function(event) {
    event.preventDefault();
});

document.getElementById('filter-form').addEventListener('submit', function(event) {
    event.preventDefault();
});
