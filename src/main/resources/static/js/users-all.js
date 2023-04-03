(() => {
	$(document).ready(() => {
		const $table = $('.table').empty();
		let index=1;
		$table
			.append($('<thead class="font-weight-bold">')
				.append($('<tr>')
					.append($('<th scope="col">').text("#"))
					.append($('<th scope="col">').text("Username"))
					.append($('<th scope="col">').text("Email"))
					.append($('<th scope="col">').text("Authorities"))
					.append($('<th scope="col">').text("Action"))
					.append($('<th scope="col">'))
				))
			.append($('<tbody>')
			);

		fetch('/api/users')
			.then(res => res.json())
			.then(res => res.forEach((user) => {
				$table.find('tbody')
					.append($('<tr class="brush-script">')
						.append($('<th class="brush-script" scope="row">').text(index))
						.append($('<td class="brush-script">').text(user.username))
						.append($('<td class="brush-script">').text(user.email))
						.append($('<td class="brush-script">').text(parseRoles(user.authorities)))
						.append($('<td class="brush-script">')
							.append($('<a class="btn btn-danger font-weight-bold text-white">Delete</a>')
								//<i class="fas fa-user-edit"></i> Profile</a>')
								.attr("href", "/users/delete/" + user.id)
	
								))
						);
						index++;
					})).catch(err => console.log(err));
	});

		function parseRoles(roles) {
			return roles
				.map(r => {
					return r.authority.startsWith("ROOT") ? r.authority
						: r.authority.split("_")[1];
				})
				.sort()
				.join(", ");
		}

})();
