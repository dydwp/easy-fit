let currentTab = null;

async function loadCategories() {
    try {
        const res = await fetch('/api/categories');
        if(!res.ok) throw new Error('요청 실패: ' + res.status);
        const categories = await res.json();
        renderTabs(categories);

        if(categories.length > 0) {
            currentTab = categories[0].code;
            loadWorkouts(currentTab);
        }
    }catch(err) {
        console.error(err);
    }
}

function renderTabs(categories) {
    const container = document.getElementById('tabs-container');
    container.innerHTML = categories.map((c, i) => `
        <button class="tab ${i === 0 ? 'active' : ''}" data-part="${c.code}" onclick="selectTab(this,'${c.code}')">
        ${c.label}
        </button>
    `).join('');
}

function selectTab(btn, part) {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    btn.classList.add('active');
    currentTab = part;
    loadWorkouts(part);
}

async function loadWorkouts(part) {
    const grid = document.getElementById('workout-grid');
    grid.innerHTML = '<p>불러오는 중...</p>';

    try {
        const res = await fetch(`/api/workouts/${part}`);
        if(!res.ok) throw new Error('요청 실패: ' + res.status);
        const items = await res.json();
        renderWorkouts(part, items);
    } catch(err) {
        grid.innerHTML = '<p>운동 데이터를 불러오지 못했습니다.</p>';
        console.error(err);
    }
}

function renderWorkouts(part, items) {
    const grid = document.getElementById('workout-grid');

    if(items.length === 0) {
        grid.innerHTML = '<p>등록된 운동이 없습니다.</p>';
        return;
    }

    grid.innerHTML = items.map((w, i) => `
        <div class="workout-card" id="wcard-${part}-${i}">
            <div class="workout-thumb">
                <img src="${w.imgUrl}" alt="${w.title}">
            </div>
            <div class="workout-body">
                <div class="workout-title">${w.title}</div>
                <div class="workout-meta">
                    ${(w.tags || '').split(',').filter(Boolean).map(t => `<span class="tag">${t}</span>`).join('')}
                </div>
                <div class="accordion-toggle" onclick="toggleAccordion('${part}-${i}')">
                    <span class="toggle-hint">자세히 보기</span>
                    <span class="toggle-arrow">▼</span>
                </div>
                <div class="accordion" id="acc-${part}-${i}">
                    <div class="accordion-inner">
                        <div class="accordion-section">
                            <div class="accordion-label">📌 정확한 자세</div>
                            <div class="accordion-text">${w.pose}</div>
                        </div>
                        <div class="accordion-section">
                            <div class="accordion-label">⚡ 자극 부위</div>
                            <div class="accordion-text">${w.targetMuscle}</div>
                        </div>
                        <div class="accordion-section">
                            <div class="accordion-label">⚠️ 주의사항</div>
                            <div class="accordion-text">${w.caution}</div>
                        </div>
                        <div class="accordion-section" style="margin-bottom: 0;">
                            <a href="${w.videoUrl}" target="_blank" class="video-btn">▶️ 영상으로 자세 확인하기</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `).join('');
}

function toggleAccordion(id) {
    const acc = document.getElementById('acc-' + id);
    const card = acc.closest('.workout-card');
    const isOpen = acc.classList.contains('open');

    document.querySelectorAll('.accordion').forEach(a => a.classList.remove('open'));
    document.querySelectorAll('.workout-card').forEach(c => c.classList.remove('open'));

    if(!isOpen) {
        acc.classList.add('open');
        card.classList.add('open');
    }
}

/* INIT */
loadCategories();