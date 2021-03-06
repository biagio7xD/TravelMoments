export default function authHeader() {
    let user = JSON.parse(localStorage.getItem('user'));
    if (user && user.accessToken) {
        return {
            'Authorization': 'Bearer ' + user.accessToken,
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*'
        };
    } else {
        return {};
    }
}