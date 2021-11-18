import {
    USER_LOADED,
    AUTH_ERROR,
    LOGIN_SUCCESS,
    LOGIN_FAIL,
    LOGOUT,
    CLEAR_ERRORS,
    REGISTER_SUCCESS,
    UPDATE_USER_FAIL,
    UPDATE_USER,
    GET_USERS,
    BLOCK_USER,
    UNBLOCK_USER
} from '../types';

const userReducer = (state, action) => {
    switch (action.type) {
        case LOGIN_SUCCESS:
            localStorage.setItem('user', JSON.stringify(action.payload));
            return {
                ...state,
                ...action.payload,
                isAuthenticated: true,
                loading: false,
                user: action.payload
            };
        case LOGIN_FAIL:
        case LOGOUT:
            localStorage.removeItem('user');
            localStorage.removeItem('forum');
            localStorage.removeItem('forumThread');
            return {
                ...state,
                isAuthenticated: false,
                loading: false,
                user: null,
                error: action.payload
            };
        case UPDATE_USER:
            localStorage.setItem('user', JSON.stringify(action.payload));
            return {
                ...state,
                user: action.payload
            };
        case UPDATE_USER_FAIL:
            return {
                ...state,
                loading: false,
                error: action.payload
            };
        case BLOCK_USER:
            return {
                ...state,
                users: state.users.map((user) =>
                    user.id === action.payload.id
                        ? { ...user, isBlocked: true }
                        : user
                )
            };
        case UNBLOCK_USER:
            return {
                ...state,
                users: state.users.map((user) =>
                    user.id === action.payload.id
                        ? { ...user, isBlocked: false }
                        : user
                )
            };
        case GET_USERS:
            return {
                ...state,
                users: action.payload
            };
        case REGISTER_SUCCESS:
            return {
                ...state,
                loading: false,
                user: action.payload
            };
        case CLEAR_ERRORS:
            return {
                ...state,
                error: null
            };

        default:
            return state;
    }
};

export default userReducer;
