export default function getUserId() {
    let user = JSON.parse(localStorage.getItem('user'));
    return user === null ? undefined : user.id;
}
