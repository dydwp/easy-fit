const observer = new IntersectionObserver((entries) => {
	entries.forEach(entry => {
		if(entry.isIntersecting) {
			entry.target.classList.add('reveal');
		}else {
			entry.target.classList.remove('reveal');
		}
	});
}, {threshold: 0.1});

document.querySelectorAll('.feat-card').forEach(card => observer.observe(card));