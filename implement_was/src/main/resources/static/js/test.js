function test() {
	console.log('test yayayaya');
}

async function load() {
	const data = await fetch("/hello", { method:"POST" }).then(d => d);
	
	console.log('data', data)
}

window.onload = function () {
	console.log('hello test 2222');
	test();
	
	load();
}
