function test() {
	console.log('test yayayaya');
}

async function load() {
	const data = await fetch("/hello", { method:"POST" }).then(d => d.text());
	
	console.log('data', data)
}

window.onload = function () {
	test();
	
	load();
}
