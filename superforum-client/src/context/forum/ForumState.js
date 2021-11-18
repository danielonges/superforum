import React, { useReducer } from 'react';
import axios from 'axios';
import ForumContext from './forumContext';
import forumReducer from './forumReducer';
import {
    CLEAR_ERRORS,
    FORUM_ERROR,
    CREATE_FORUM,
    UPDATE_FORUM,
    DELETE_FORUM,
    SET_FORUM,
    SET_FORUM_THREAD,
    CREATE_FORUM_THREAD,
    GET_ALL_FORUMS,
    GET_ALL_FORUM_CATEGORIES,
    GET_FORUMS_BY_CATEGORY,
    GET_FORUMS_BY_TITLE,
    GET_USER_FORUM_THREADS,
    UPDATE_FORUM_THREAD,
    DELETE_FORUM_THREAD,
    CREATE_POST,
    GET_FORUM_THREAD,
    UPDATE_POST,
    DELETE_POST,
    CLOSE_FORUM_THREAD,
    INCREMENT_VIEW_COUNT
} from '../types';

const ForumState = (props) => {
    const initialState = {
        loading: true,
        forum: JSON.parse(localStorage.getItem('forum')),
        forums: null,
        forumCategories: null,
        forumThread: JSON.parse(localStorage.getItem('forumThread')),
        forumThreads: null,
        error: null
    };

    const [state, dispatch] = useReducer(forumReducer, initialState);

    const createForum = async (formData, adminId) => {
        const config = {
            headers: {
                'Content-Type': 'application/json',
                adminId: adminId
            }
        };

        try {
            const res = await axios.post('/forum', formData, config);

            dispatch({
                type: CREATE_FORUM,
                payload: res.data
            });
            getAllForums();
            // return res.data;
        } catch (error) {
            dispatch({
                type: FORUM_ERROR,
                payload: error.response.data.error
            });
        }
    };

    const setForum = async (forum) => {
        dispatch({
            type: SET_FORUM,
            payload: forum
        });
    };

    const updateForum = async (forum, adminId) => {
        const config = {
            headers: {
                'Content-Type': 'application/json',
                adminId: adminId
            }
        };

        try {
            await axios.put(`/forum/${forum.id}`, forum, config);
            await getAllForums();
            dispatch({
                type: UPDATE_FORUM
            });
        } catch (error) {
            dispatch({
                type: FORUM_ERROR,
                payload: error.response.data.error
            });
        }
    };

    const deleteForum = async (forumId, adminId) => {
        const config = {
            headers: {
                'Content-Type': 'application/json',
                adminId: adminId
            }
        };

        try {
            await axios.delete(`/forum/${forumId}`, config);

            dispatch({
                type: DELETE_FORUM,
                payload: forumId
            });
            getAllForums();
        } catch (error) {
            dispatch({
                type: FORUM_ERROR,
                payload: error.response.data.error
            });
        }
    };

    // gets all forums available
    const getAllForums = async () => {
        const res = await axios.get('/forum');

        dispatch({
            type: GET_ALL_FORUMS,
            payload: res.data
        });
    };

    const getForumsByTitle = async (title) => {
        const res = await axios.get(`/forum/title-query?title=${title}`);

        dispatch({
            type: GET_FORUMS_BY_TITLE,
            payload: res.data
        });
    };

    // gets all forum categories
    const getAllForumCategories = async () => {
        const res = await axios.get('/forum/forumcategory');

        dispatch({
            type: GET_ALL_FORUM_CATEGORIES,
            payload: res.data
        });
    };

    const createForumThread = async (forumThread, forumId) => {
        try {
            const res = await axios.post(
                `/forum/${forumId}/forumthread`,
                forumThread
            );

            await getAllForums();
            dispatch({
                type: CREATE_FORUM_THREAD,
                payload: res.data
            });
        } catch (error) {
            dispatch({
                type: FORUM_ERROR,
                payload: error.response.data.error
            });
        }
    };

    const setForumThread = (forumThread) => {
        dispatch({
            type: SET_FORUM_THREAD,
            payload: forumThread
        });
    };

    const getForumThread = async (forumThreadId) => {
        try {
            const res = await axios.get(`/forum/forumthread/${forumThreadId}`);
            dispatch({
                type: GET_FORUM_THREAD,
                payload: res.data
            });
        } catch (error) {
            dispatch({
                type: FORUM_ERROR,
                payload: error.response.data.error
            });
        }
    };

    const updateForumThread = async (forumThread, userId) => {
        const config = {
            headers: {
                'Content-Type': 'application/json',
                userId: userId
            }
        };

        try {
            await axios.put(
                `/forum/forumthread/${forumThread.id}`,
                forumThread,
                config
            );
            await getAllForums();
            dispatch({
                type: UPDATE_FORUM_THREAD,
                payload: forumThread
            });
        } catch (error) {
            dispatch({
                type: FORUM_ERROR,
                payload: error.response.data.error
            });
        }
    };

    const deleteForumThread = async (forumThreadId, userId) => {
        const config = {
            headers: {
                'Content-Type': 'application/json',
                userId: userId
            }
        };

        try {
            const res = await axios.delete(
                `/forum/forumthread/${forumThreadId}`,
                config
            );
            await getAllForums();
            dispatch({
                type: DELETE_FORUM_THREAD
            });
        } catch (error) {
            dispatch({
                type: FORUM_ERROR,
                payload: error.response.data.error
            });
        }
    };

    const closeForumThread = async (forumThread, user, closed) => {
        const config = {
            headers: {
                userId: user.id
            }
        };

        try {
            await axios.put(
                `/forum/forumthread/close?closed=${closed}`,
                forumThread,
                config
            );
            dispatch({
                type: CLOSE_FORUM_THREAD,
                payload: {
                    ...forumThread,
                    isClosed: closed,
                    closedBy: closed ? user : null
                }
            });
        } catch (error) {
            dispatch({
                type: FORUM_ERROR,
                payload: error.response.data.error
            });
        }
    };

    const incrementViewCount = async (forumThread) => {
        try {
            await axios.put('/forum/forumthread/view', forumThread);
            await getAllForums();
            dispatch({
                type: INCREMENT_VIEW_COUNT,
                payload: forumThread
            });
        } catch (error) {
            dispatch({
                type: FORUM_ERROR,
                payload: error.response.data.error
            });
        }
    };

    // gets user recent forum threads
    const getUserRecentForumThreads = async (userId) => {
        const config = {
            headers: {
                userId: userId
            }
        };

        try {
            const res = await axios.get('/forum/forumthread', config);

            dispatch({
                type: GET_USER_FORUM_THREADS,
                payload: res.data
            });
        } catch (error) {
            dispatch({
                type: FORUM_ERROR,
                payload: error.response.data.error
            });
        }
    };

    // gets forums by category
    const getForumsByCategory = async (category) => {
        try {
            const res = await axios.get(
                `forum/category-query?category=${category}`
            );

            dispatch({
                type: GET_FORUMS_BY_CATEGORY,
                payload: res.data
            });
        } catch (error) {
            dispatch({
                type: FORUM_ERROR,
                payload: error.response.data.error
            });
        }
    };

    const createPost = async (post, forumThreadId) => {
        try {
            const res = await axios.post(
                `/forum/forumthread/${forumThreadId}/posts`,
                post
            );
            // await getForumThread(forumThreadId);
            dispatch({
                type: CREATE_POST,
                payload: res.data
            });
        } catch (error) {
            dispatch({
                type: FORUM_ERROR,
                payload: error.response.data.error
            });
        }
    };

    const updatePost = async (post, userId) => {
        const config = {
            headers: {
                userId: userId
            }
        };

        try {
            const res = await axios.put(
                `/forum/posts/${post.id}`,
                post,
                config
            );
            dispatch({
                type: UPDATE_POST,
                payload: post
            });
        } catch (error) {
            dispatch({
                type: FORUM_ERROR,
                payload: error.response.data.error
            });
        }
    };

    const deletePost = async (postId, userId) => {
        const config = {
            headers: {
                userId: userId
            }
        };
        try {
            await axios.delete(`/forum/posts/${postId}`, config);
            dispatch({
                type: DELETE_POST,
                payload: postId
            });
        } catch (error) {
            dispatch({
                type: FORUM_ERROR,
                payload: error.response.data.error
            });
        }
    };

    // clear errors
    const clearErrors = () => dispatch({ type: CLEAR_ERRORS });

    return (
        <ForumContext.Provider
            value={{
                loading: state.loading,
                forum: state.forum,
                forums: state.forums,
                forumCategories: state.forumCategories,
                forumThread: state.forumThread,
                forumThreads: state.forumThreads,
                error: state.error,
                createForum,
                updateForum,
                deleteForum,
                setForum,
                setForumThread,
                getForumThread,
                createForumThread,
                updateForumThread,
                deleteForumThread,
                closeForumThread,
                incrementViewCount,
                getAllForums,
                getAllForumCategories,
                getUserRecentForumThreads,
                getForumsByCategory,
                getForumsByTitle,
                createPost,
                updatePost,
                deletePost,
                clearErrors
            }}
        >
            {props.children}
        </ForumContext.Provider>
    );
};

export default ForumState;
