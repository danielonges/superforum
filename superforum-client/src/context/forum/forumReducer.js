import {
    CLEAR_ERRORS,
    CREATE_FORUM,
    UPDATE_FORUM,
    DELETE_FORUM,
    SET_FORUM,
    SET_FORUM_THREAD,
    CREATE_FORUM_THREAD,
    FORUM_ERROR,
    GET_ALL_FORUMS,
    GET_ALL_FORUM_CATEGORIES,
    GET_FORUMS_BY_CATEGORY,
    GET_FORUMS_BY_TITLE,
    GET_USER_FORUM_THREADS,
    UPDATE_FORUM_THREAD,
    DELETE_FORUM_THREAD,
    CREATE_POST,
    UPDATE_POST,
    DELETE_POST,
    CLOSE_FORUM_THREAD,
    INCREMENT_VIEW_COUNT
} from '../types';

const forumReducer = (state, action) => {
    switch (action.type) {
        case CREATE_FORUM:
        case SET_FORUM:
            localStorage.setItem('forum', JSON.stringify(action.payload));
            return {
                ...state,
                forum: action.payload
            };
        case UPDATE_FORUM: {
            const updatedForum = state.forums.find(
                (currForum) => currForum.id === state.forum.id
            );
            localStorage.setItem('forum', JSON.stringify(updatedForum));
            return {
                ...state,
                forum: updatedForum
            };
        }
        case DELETE_FORUM:
            localStorage.removeItem('forum');
            return {
                ...state,
                forum: null,
                forums: state.forums.filter(
                    (currForum) => currForum.id !== action.payload
                )
            };
        case SET_FORUM_THREAD:
            localStorage.setItem('forumThread', JSON.stringify(action.payload));
            return {
                ...state,
                forumThread: action.payload
            };
        case CREATE_FORUM_THREAD: {
            const updatedForum = state.forums.find(
                (currForum) => currForum.id === state.forum.id
            );
            localStorage.setItem('forum', JSON.stringify(updatedForum));
            localStorage.setItem('forumThread', JSON.stringify(action.payload));
            return {
                ...state,
                forum: updatedForum,
                forumThread: action.payload
            };
        }
        case UPDATE_FORUM_THREAD: {
            const updatedForum = state.forums.find(
                (currForum) => currForum.id === state.forum.id
            );
            localStorage.setItem('forum', JSON.stringify(updatedForum));
            localStorage.setItem('forumThread', JSON.stringify(action.payload));
            return {
                ...state,
                forum: updatedForum,
                forumThread: action.payload
            };
        }
        case DELETE_FORUM_THREAD: {
            const updatedForum = state.forums.find(
                (currForum) => currForum.id === state.forum.id
            );
            localStorage.setItem('forum', JSON.stringify(updatedForum));
            localStorage.removeItem('forumThread');
            return {
                ...state,
                forum: updatedForum,
                forumThread: null
            };
        }
        case CLOSE_FORUM_THREAD: {
            const updatedForum = {
                ...state.forum,
                forumThreads: state.forum.forumThreads.map((ft) =>
                    ft.id === action.payload.id ? action.payload : ft
                )
            };
            localStorage.setItem('forum', JSON.stringify(updatedForum));
            localStorage.setItem('forumThread', JSON.stringify(action.payload));
            return {
                ...state,
                forum: updatedForum,
                forumThread: action.payload
            };
        }
        case INCREMENT_VIEW_COUNT: {
            const updatedForumThread = {
                ...action.payload,
                views: action.payload.views + 1
            };
            const updatedForum = state.forums.find(
                (currForum) => currForum.id === state.forum.id
            );
            console.log(updatedForum);
            localStorage.setItem('forum', JSON.stringify(updatedForum));
            localStorage.setItem(
                'forumThread',
                JSON.stringify(updatedForumThread)
            );
            return {
                ...state,
                forum: updatedForum,
                forumThread: updatedForumThread
            };
        }
        case GET_ALL_FORUMS:
        case GET_FORUMS_BY_CATEGORY:
        case GET_FORUMS_BY_TITLE:
            return {
                ...state,
                forums: action.payload
            };
        case GET_ALL_FORUM_CATEGORIES:
            return {
                ...state,
                forumCategories: action.payload
            };
        case GET_USER_FORUM_THREADS:
            return {
                ...state,
                forumThreads: action.payload
            };
        case CREATE_POST:
            return {
                ...state,
                forumThread: {
                    ...state.forumThread,
                    posts: [...state.forumThread.posts, action.payload]
                }
            };
        case UPDATE_POST: {
            const updatedForumThread = {
                ...state.forumThread,
                posts: [...state.forumThread.posts].map((currPost) =>
                    currPost.id === action.payload.id
                        ? action.payload
                        : currPost
                )
            };
            localStorage.setItem(
                'forumThread',
                JSON.stringify(updatedForumThread)
            );
            return {
                ...state,
                forumThread: updatedForumThread
            };
        }
        case DELETE_POST: {
            const updatedForumThread = {
                ...state.forumThread,
                posts: [...state.forumThread.posts].filter(
                    (currPost) => currPost.id !== action.payload
                )
            };
            localStorage.setItem(
                'forumThread',
                JSON.stringify(updatedForumThread)
            );
            return {
                ...state,
                forumThread: updatedForumThread
            };
        }
        case FORUM_ERROR:
            return {
                ...state,
                error: action.payload
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

export default forumReducer;
