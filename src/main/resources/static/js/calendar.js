const BODY_PARTS = [
    { code: 'chest',    emoji: '🫁', label: '가슴' },
    { code: 'back',     emoji: '🦾', label: '등' },
    { code: 'leg',      emoji: '🦵', label: '하체' },
    { code: 'shoulder', emoji: '🏋️', label: '어깨' },
    { code: 'arm',      emoji: '💪', label: '팔' },
    { code: 'core',     emoji: '🔥', label: '코어' },
];
const EMOJI_BY_CODE = Object.fromEntries(BODY_PARTS.map(p => [p.code, p.emoji]));

let selectedParts = [];

let calYear, calMonth;
let selectedDate = null;
let monthRecords = {};
let maxStreak = 0;

function initCalendar() {
    const now = new Date();
    calYear = now.getFullYear();
    calMonth = now.getMonth();
}

function changeMonth(dir) {
    calMonth += dir;
    if(calMonth > 11) {
        calMonth = 0;
        calYear++;
    } else if(calMonth < 0) {
        calMonth = 11;
        calYear--;
    }
    loadMonth();
}

function dateKey(year, month, day) {
    return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
}

async function loadMonth() {
    try {
        const res = await fetch(`/api/calendar/month?year=${calYear}&month=${calMonth + 1}`);
        if(!res.ok) throw new Error('요청 실패: ' + res.status);
        const data = await res.json();

        monthRecords = {};
        (data.records || []).forEach(r => {
            monthRecords[r.recordDate] = {
				stamped: r.stamped,
				memo: r.memo,
				bodyParts: r.bodyParts ? r.bodyParts.split(',') : []
			};
        });
        maxStreak = data.maxStreak || 0;

        renderCalendar();
    } catch(err) {
        console.error(err);
    }
}

function renderCalendar() {
    const monthNames = ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'];
    document.getElementById('cal-title').textContent = `${calYear}년 ${monthNames[calMonth]}`;

    const firstDayOfWeek = new Date(calYear, calMonth, 1).getDay();
    const totalDays = new Date(calYear, calMonth + 1, 0).getDate();
    const today = new Date();
    const container = document.getElementById('cal-days');

    let html = '';

    for(let i = 0; i < firstDayOfWeek; i++) {
        html += `<div class="cal-day empty"></div>`;
    }

    for(let d = 1; d <= totalDays; d++) {
        const key = dateKey(calYear, calMonth + 1, d);
        const isToday = (
            d === today.getDate() &&
            calMonth === today.getMonth() &&
            calYear === today.getFullYear()
        );
        const record = monthRecords[key];
        const isStamped = !!(record && record.stamped);
        const isSelected = selectedDate === key;
		const memo = record ? record.memo : '';
		const bodyParts = record ? (record.bodyParts || []) : [];

		const classes = ['cal-day'];
		if(isToday) classes.push('today');
		if(isStamped) classes.push('stamped');
		else if(isSelected) classes.push('selected');

		const emojiHtml = bodyParts.length
		    ? `<div class="cal-day-emojis">${bodyParts.map(c => EMOJI_BY_CODE[c] || '').join('')}</div>`
		    : '';
		const memoDotHtml = memo ? `<div class="cal-day-memo-dot" title="${memo}"></div>` : '';

		html += `<div class="${classes.join(' ')}" onclick="selectDay('${key}')">
		            <span class="cal-day-num">${d}</span>
		            ${emojiHtml}
		            ${memoDotHtml}
		        </div>`;
    }

    container.innerHTML = html;
    document.getElementById('streak-count').textContent = maxStreak;
}

function selectDay(key) {
    selectedDate = key;
	const record = monthRecords[key];
	selectedParts = record ? [...(record.bodyParts || [])] : [];
    renderCalendar();

    const parts = key.split('-');
    document.getElementById('selected-date-label').textContent =
        `${parts[0]}년 ${parseInt(parts[1])}월 ${parseInt(parts[2])}일`;

    updateMemoPanel();
    document.getElementById('memo-input').focus();
}

function renderPartPicker() {
    const container = document.getElementById('part-picker');
    container.innerHTML = BODY_PARTS.map(p => `
        <div class="part-chip ${selectedParts.includes(p.code) ? 'active' : ''}" onclick="togglePart('${p.code}')">
            <span class="emoji">${p.emoji}</span><span>${p.label}</span>
        </div>
    `).join('');
}

function togglePart(code) {
    if(!selectedDate) return;
    const idx = selectedParts.indexOf(code);
    if(idx >= 0) selectedParts.splice(idx, 1);
    else selectedParts.push(code);
    renderPartPicker();
}

function updateMemoPanel() {
    if(!selectedDate) return;

    const record = monthRecords[selectedDate];
    const isStamped = !!(record && record.stamped);
    const btn = document.getElementById('stamp-btn');

    btn.textContent = isStamped ? '오운완 완료! ✓' : '오운완 스탬프 찍기 ✓';
    btn.className = 'memo-stamp-btn' + (isStamped ? ' stamped' : '');
    btn.disabled = false;

    document.getElementById('memo-input').value = record ? (record.memo || '') : '';
	renderPartPicker();
}

async function toggleStamp() {
    if(!selectedDate) return;

    try {
        const res = await fetch(`/api/calendar/${selectedDate}/stamp`, { method: 'POST' });
        if(!res.ok) throw new Error('요청 실패: ' + res.status);

        await loadMonth();
        updateMemoPanel();
    } catch(err) {
        console.error(err);
    }
}

async function saveMemo() {
    if(!selectedDate) return;

    const memoText = document.getElementById('memo-input').value;

    try {
        const res = await fetch(`/api/calendar/${selectedDate}/memo`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ memo: memoText, bodyParts: selectedParts })
        });
        if(!res.ok) throw new Error('요청 실패: ' + res.status);
        const updated = await res.json();

		monthRecords[selectedDate] = {
		    stamped: updated.stamped,
		    memo: updated.memo,
		    bodyParts: updated.bodyParts ? updated.bodyParts.split(',') : []
		};

        const btn = document.querySelector('.memo-save-btn');
        btn.textContent = '✅ 저장됨!';
        setTimeout(() => { btn.textContent = '💾 저장하기'; }, 1500);

        renderCalendar();
    } catch(err) {
        console.error(err);
    }
}

initCalendar();
loadMonth();